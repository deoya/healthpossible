package com.hye.mission.ui.analyzer.exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.hye.mission.ui.analyzer.AiPoseAnalyzer
import com.hye.mission.ui.analyzer.AnalyzerState
import kotlin.math.abs


/**
 * [스쿼트 로직]
 */
internal fun AiPoseAnalyzer.checkSquatLogic(pose: Pose) {
    val lShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!
    val lHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)!!
    val lKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)!!
    val lAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)!!
    val rShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)!!
    val rHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)!!
    val rKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)!!
    val rAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)!!

    // 1. 무릎 각도 (앉은 정도)
    val avgAngle = (calculate3DAngle(lHip, lKnee, lAnkle) + calculate3DAngle(rHip, rKnee, rAnkle)) / 2.0
    // 2. 상체 각도 (허리 숙임)
    val avgBackAngle = (calculate3DAngle(lShoulder, lHip, lKnee) + calculate3DAngle(rShoulder, rHip, rKnee)) / 2.0

    // [추가 기능] 무릎 쏠림(Valgus) 체크
    // 정면 카메라 기준: 무릎의 X좌표가 발목보다 안쪽(몸 중심쪽)으로 과도하게 쏠렸는지 확인
    // 간단한 로직: 무릎 사이 거리 < 발목 사이 거리 (현저하게 차이날 때)
    val kneeDist = abs(lKnee.position.x - rKnee.position.x)
    val ankleDist = abs(lAnkle.position.x - rAnkle.position.x)

    // 무릎 쏠림 경고 메시지 가져오기
    var warningMsg = ""
    if (currentState == AnalyzerState.EXERCISING && avgAngle < 150.0) {
        // 무릎 쏠림 (Valgus) 체크
        val kneeDist = abs(lKnee.position.x - rKnee.position.x)
        val ankleDist = abs(lAnkle.position.x - rAnkle.position.x)
        if (kneeDist < ankleDist * 0.75) {
            warningMsg = typeMessages.errorKnee.random()
        }
        // 상체 숙임 체크
        else if (avgBackAngle < 70.0) {
            warningMsg = typeMessages.errorBack.random()
        }
    }

    // 공통 사이클 처리
    processRepetitionCycle(
        currentMetric = avgAngle,
        isReadyPose = { it > 165.0 },
        isPeakPose = { it < 100.0 },
        isReturnPose = { it > 160.0 },
        warningMessage = warningMsg
    )
}