package com.hye.mission.ui.analyzer

import android.content.Context
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.hye.domain.model.mission.feedback.AgentMessageSet
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.features.mission.R
import com.hye.mission.ui.analyzer.exercise.checkLungeLogic
import com.hye.mission.ui.analyzer.exercise.checkSquatLogic
import com.hye.mission.ui.util.AgentMessageProvider
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

// 상태 정의 (State Machine)
enum class AnalyzerState {
    IDLE,       // 대기 (작전 대기)
    READY,      // 준비 완료 (작전 개시)
    EXERCISING  // 운동 중 (침투 및 탈출)
}

// 운동 진행 단계 (하강 -> 유지 -> 상승)
enum class MotionPhase {
    NONE,
    DESCENT,    // 하강 (침투)
    BOTTOM,     // 최저점 (목표 확보)
    ASCENT      // 상승 (탈출)
}

class AiPoseAnalyzer(
    val context: Context, // ✅ Context 추가 (리소스 접근용)
    val aiPoseAnalyzerTagName: String,
    val exerciseType: AiExerciseType,
    val poseDetector: PoseDetector,
    val onFeedback: (String) -> Unit,
    val onRepCounted: () -> Unit,
    val onPoseDetected: (Pose, Int, Int) -> Unit,
    val targetCount: Int = 10
) : ImageAnalysis.Analyzer {
    // --- 상태 변수 ---
    var currentState = AnalyzerState.IDLE
    var currentPhase = MotionPhase.NONE
    var isPeakReached = false
    var currentRepCount = 0 // 현재 수행 횟수 추적
    var previousAngle = 180.0 // 이전 프레임 각도 (움직임 방향 판별용)
    // --- 타이밍 변수 ---
    var lastFeedbackTime = 0L
    var readyTimestamp = 0L
    var messageBlockTimestamp = 0L
    // --- 대상 소실 감지 ---
    var missingFrameCount = 0
    val MISSING_THRESHOLD = 30
    // --- 메시지 세트 로딩 ---
    val agentMessage = AgentMessageProvider(context)
    val typeMessages: AgentMessageSet = agentMessage.getExerciseSet(exerciseType)

    init {
        Timber.tag(aiPoseAnalyzerTagName).d("미션: $exerciseType")
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            poseDetector.process(image)
                .addOnSuccessListener { pose ->
                    val width = if (imageProxy.imageInfo.rotationDegrees == 90 || imageProxy.imageInfo.rotationDegrees == 270) image.height else image.width
                    val height = if (imageProxy.imageInfo.rotationDegrees == 90 || imageProxy.imageInfo.rotationDegrees == 270) image.width else image.height

                    try {
                        // 1. 사람 존재 여부 체크
                        if (!checkPersonInFrame(pose)) {
                            onPoseDetected(pose, width, height)
                            return@addOnSuccessListener
                        }
                        // Todo : 분리 필요
                        // 2. 카테고리 별 로직 분기
                        when (exerciseType) {
                            AiExerciseType.SQUAT -> checkSquatLogic(pose)
                            AiExerciseType.LUNGE -> checkLungeLogic(pose)
                            else -> {} // 추후 추가
                        }
                    } catch (e: Exception) {
                        Timber.tag(aiPoseAnalyzerTagName).e(e, "Analysis Error")
                    }
                    onPoseDetected(pose, width, height)
                }
                .addOnCompleteListener { imageProxy.close() }
        } else {
            imageProxy.close()
        }
    }

    fun checkPersonInFrame(pose: Pose): Boolean {
        val requiredLandmarks = listOf(
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
            pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
            pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
        )

        if (requiredLandmarks.any { it == null || it.inFrameLikelihood < 0.5f }) {
            missingFrameCount++
            if (missingFrameCount > MISSING_THRESHOLD) {
                resetState(agentMessage.missing)
            }
            return false
        }
        missingFrameCount = 0
        return true
    }

    fun processRepetitionCycle(
        currentMetric: Double,
        isReadyPose: (Double) -> Boolean,
        isPeakPose: (Double) -> Boolean,
        isReturnPose: (Double) -> Boolean,
        warningMessage: String
    ) {
        val currentTime = System.currentTimeMillis()
        val angleDelta = currentMetric - previousAngle
        previousAngle = currentMetric

        when (currentState) {
            AnalyzerState.IDLE -> {
                val readyAngle = if (readyTimestamp > 0) 155.0 else 165.0 // 히스테리시스
                if (isReadyPose(currentMetric)) {
                    if (readyTimestamp == 0L) {
                        readyTimestamp = currentTime
                        sendFeedback(context.getString(R.string.mission_status_identifying), force = true)
                    } else if (currentTime - readyTimestamp > 1000) {
                        currentState = AnalyzerState.READY
                        sendFeedback(context.getString(R.string.mission_status_ready), force = true, keepDuration = 2000)
                    }
                } else {
                    if (readyTimestamp != 0L) {
                        sendFeedback(context.getString(R.string.mission_error_stand_straight), force = true)
                        readyTimestamp = 0L
                    }
                }
            }

            AnalyzerState.READY, AnalyzerState.EXERCISING -> {
                currentState = AnalyzerState.EXERCISING

                // 1. 긴급 경고 (최우선)
                if (warningMessage.isNotEmpty()) {
                    sendFeedback(warningMessage, force = true, keepDuration = 1000)
                    return
                }

                // 2. 단계별 브리핑
                if (currentTime > messageBlockTimestamp) {
                    // 하강 (Descent)
                    if (angleDelta < -1.0 && currentMetric > 100.0 && currentPhase != MotionPhase.DESCENT) {
                        currentPhase = MotionPhase.DESCENT
                        sendFeedback(getDescentMessage())
                    }
                    // 최저점 (Bottom)
                    else if (isPeakPose(currentMetric)) {
                        if (currentPhase != MotionPhase.BOTTOM) {
                            currentPhase = MotionPhase.BOTTOM
                            sendFeedback(typeMessages.bottom.random())
                            isPeakReached = true
                        }
                    }
                    // 상승 (Ascent)
                    else if (angleDelta > 1.0 && isPeakReached && currentMetric > 110.0 && currentPhase != MotionPhase.ASCENT) {
                        currentPhase = MotionPhase.ASCENT
                        sendFeedback(typeMessages.ascent.random())
                    }
                }

                // 3. 완료 판정
                if (isPeakReached && isReturnPose(currentMetric)) {
                    isPeakReached = false
                    currentPhase = MotionPhase.NONE
                    currentRepCount++
                    onRepCounted()

                    val successMsg = getSuccessMessage(currentRepCount, targetCount)
                    sendFeedback(successMsg, force = true, keepDuration = 1500)
                }
            }
        }
    }

    fun resetState(reason: String) {
        if (currentState != AnalyzerState.IDLE) {
            Timber.tag(aiPoseAnalyzerTagName).d("Reset: $reason")
            currentState = AnalyzerState.IDLE
            currentPhase = MotionPhase.NONE
            isPeakReached = false
            readyTimestamp = 0L

            val msg = if (reason.contains("Missing")) context.getString(R.string.mission_agent_missing)
            else context.getString(R.string.mission_error_return_to_screen)
            sendFeedback(msg, force = true)
        }
    }
    fun calculate3DAngle(first: PoseLandmark, mid: PoseLandmark, last: PoseLandmark): Double {
        val ax = first.position3D.x - mid.position3D.x
        val ay = first.position3D.y - mid.position3D.y
        val az = first.position3D.z - mid.position3D.z
        val cx = last.position3D.x - mid.position3D.x
        val cy = last.position3D.y - mid.position3D.y
        val cz = last.position3D.z - mid.position3D.z
        val dotProduct = (ax * cx + ay * cy + az * cz)
        val magA = sqrt(ax * ax + ay * ay + az * az)
        val magC = sqrt(cx * cx + cy * cy + cz * cz)
        if (magA == 0.0f || magC == 0.0f) return 180.0
        val cosTheta = dotProduct / (magA * magC)
        return abs(Math.toDegrees(acos(cosTheta.coerceIn(-1.0f, 1.0f)).toDouble()))
    }
}