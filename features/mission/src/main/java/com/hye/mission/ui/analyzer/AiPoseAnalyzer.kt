package com.hye.mission.ui.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.hye.domain.model.mission.types.AiExerciseType
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

class AiPoseAnalyzer(
    val aiPoseAnalyzerTagName: String,
    private val exerciseType: AiExerciseType,
    private val poseDetector: PoseDetector,
    private val onFeedback: (String) -> Unit,
    private val onRepCounted: () -> Unit, // 횟수 인정 시 호출
    private val onPoseDetected: (Pose, Int, Int) -> Unit, // 그리기 데이터 전달
    private val feedbackMessages: List<String>
) : ImageAnalysis.Analyzer {

    private var isDown = false
    private var lastFeedbackTime = 0L // 피드백 쿨타임용

    private var lastCountTime: Long = 0

    init {
        Timber.tag(aiPoseAnalyzerTagName).d("Initialized for type: $exerciseType")
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            poseDetector.process(image)
                .addOnSuccessListener { pose ->
                    // 이미지 크기 파악 (가로/세로가 회전 정보에 따라 바뀜)
                    val width = if (imageProxy.imageInfo.rotationDegrees == 90 || imageProxy.imageInfo.rotationDegrees == 270) image.height else image.width
                    val height = if (imageProxy.imageInfo.rotationDegrees == 90 || imageProxy.imageInfo.rotationDegrees == 270) image.width else image.height

                    try {
                        when (exerciseType) {
                            AiExerciseType.SQUAT -> checkSquatLogic(pose)
                            // Todo : 추후 LUNGE, PLANK 등 추가 예정
                            else -> {}
                        }
                    }catch (e:Exception){
                        Timber.tag(aiPoseAnalyzerTagName).e(e, "Logic error during analysis")
                    }
                    onPoseDetected(pose, width, height)
                }
                .addOnFailureListener { e ->
                    Timber.tag(aiPoseAnalyzerTagName).e(e, "ML Kit detection failed")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
    
    // 스쿼트 판정 로직
    // by Tutor Pyo (insoo.pyo@gmail.com)
    private fun checkSquatLogic(pose: Pose) {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP) ?: return
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE) ?: return
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE) ?: return
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP) ?: return
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE) ?: return
        val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE) ?: return

        // 3D 각도 계산
        val leftAngle = calculate3DAngle(leftHip, leftKnee, leftAnkle)
        val rightAngle = calculate3DAngle(rightHip, rightKnee, rightAnkle)
        val avgAngle = (leftAngle + rightAngle) / 2

        provideSquatFeedback(avgAngle)

        // 상태 머신 (State Machine)
        if (avgAngle < 110.0) { // 하강
            isDown = true
        } else if (isDown && avgAngle > 160.0) { // 상승
            isDown = false
            onRepCounted() // 카운트 +1
        }
    }

    private fun provideSquatFeedback(angle: Double) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastCountTime < 500) return // 0.5초마다 갱신


        // ✅ 각도별 메시지 정의 (빈 문자열이면 버블이 사라짐)
        val message = when {
            angle > 175.0 -> "" // 완전히 서 있으면 피드백 숨김 (평소 상태)
            angle > 160.0 -> feedbackMessages[0] // 살짝 무릎 굽힘
            angle in 140.0..160.0 -> feedbackMessages[1]
            angle in 100.0..140.0 -> feedbackMessages[2]
            angle < 100.0 -> feedbackMessages[3]
            else -> ""
        }

        // ✅ 빈 문자열("")도 전송해서 UI를 지워줌 (기존 if문 제거)
        onFeedback(message)
        lastFeedbackTime = currentTime
    }

    // 3D 각도 계산 함수
    // by Tutor Pyo (insoo.pyo@gmail.com)
    private fun calculate3DAngle(first: PoseLandmark, mid: PoseLandmark, last: PoseLandmark): Double {
        val ax = first.position3D.x - mid.position3D.x
        val ay = first.position3D.y - mid.position3D.y
        val az = first.position3D.z - mid.position3D.z
        val cx = last.position3D.x - mid.position3D.x
        val cy = last.position3D.y - mid.position3D.y
        val cz = last.position3D.z - mid.position3D.z

        val dotProduct = (ax * cx + ay * cy + az * cz)
        val magA = sqrt(ax * ax + ay * ay + az * az)
        val magC = sqrt(cx * cx + cy * cy + cz * cz)

        if (magA.toDouble() == 0.0 || magC.toDouble() == 0.0) return 180.0

        val cosTheta = dotProduct / (magA * magC)
        val angle = Math.toDegrees(acos(cosTheta.coerceIn(-1.0f, 1.0f)).toDouble())
        return abs(angle)
    }
}