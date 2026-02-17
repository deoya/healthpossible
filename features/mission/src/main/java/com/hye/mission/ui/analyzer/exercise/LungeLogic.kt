package com.hye.mission.ui.analyzer.exercise

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.hye.mission.ui.analyzer.AiPoseAnalyzer
import com.hye.mission.ui.analyzer.AnalyzerState
import kotlin.math.abs
import kotlin.math.min

/**
 * [런지 로직]
 * 1. 대기(Idle): 차렷 자세 -> 0.5초 유지 -> Ready
 * 2. 운동(Exercising): 한쪽 발 굽힘(Down) -> 제자리 복귀(Up) -> Count
 */
internal fun AiPoseAnalyzer.checkLungeLogic(pose: Pose) {
// 1. 관절 데이터 가져오기
    val lShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!
    val rShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)!!
    val lHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)!!
    val rHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)!!
    val lKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)!!
    val rKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)!!
    val lAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)!!
    val rAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)!!

    // 2. 각도 계산
    val leftLegAngle = calculate3DAngle(lHip, lKnee, lAnkle)
    val rightLegAngle = calculate3DAngle(rHip, rKnee, rAnkle)

    // 런지는 한쪽 다리만 굽혀지므로, 더 많이 굽혀진(각도가 작은) 다리를 기준으로 판단
    val activeLegAngle = min(leftLegAngle, rightLegAngle)

    // 차렷 자세 판별용 (둘 다 펴져 있어야 함)
    val standingMetric = (leftLegAngle + rightLegAngle) / 2.0

    // 상체 기울기 (런지는 상체를 꼿꼿이 세워야 함)
    val avgBackAngle = (calculate3DAngle(lShoulder, lHip, lKnee) + calculate3DAngle(rShoulder, rHip, rKnee)) / 2.0


    // 3. 자세 교정 경고
    var warningMsg = ""
    if (this.currentState == AnalyzerState.EXERCISING && activeLegAngle < 140.0) {
        // [경고 1] 상체 쏠림 체크 (보통 80도 미만이면 앞으로 숙인 것)
        if (avgBackAngle < 80.0) {
            warningMsg = typeMessages.errorBack.random()
        }

        // [경고 2] 무릎 쏠림 (Valgus) 체크
        // 런지 시 앞무릎이 안쪽으로 무너지면 부상 위험
        val lKneeDist = abs(lKnee.position.x - lAnkle.position.x)
        val rKneeDist = abs(rKnee.position.x - rAnkle.position.x)

        // 간단한 휴리스틱: 발목과 무릎의 X좌표 차이가 너무 크면(기울어지면) 경고
        // (정면 카메라 기준, 런지 자세에서 무릎이 수직이 아니면 쏠린 것)
        if (lKneeDist > 50 || rKneeDist > 50) {
            warningMsg = typeMessages.descentWarning.random()
        }
    }

    // 4. 상태 머신 위임
    this.processRepetitionCycle(
        currentMetric = activeLegAngle, // 움직이는 다리의 각도를 기준으로 함

        // 준비 자세: 두 다리가 모두 펴져 있어야 함 (> 160도)
        isReadyPose = { standingMetric > 160.0 },

        // Down(Peak): 한쪽 다리가 충분히 굽혀졌는가? (< 100도)
        isPeakPose = { angle -> angle < 100.0 },

        //Up(Return): 다시 두 다리가 펴졌는가? (activeAngle도 펴져야 함)
        isReturnPose = { angle -> angle > 150.0 },

        warningMessage = warningMsg
    )
}