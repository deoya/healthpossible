package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseOverlay(
    pose: Pose,
    imageWidth: Int,
    imageHeight: Int,
    isFrontCamera: Boolean = true,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val screenWidth = size.width
        val screenHeight = size.height

        // 이미지 비율에 맞춰 좌표 변환 (Scale)
        val scaleX = screenWidth / imageWidth
        val scaleY = screenHeight / imageHeight

        fun transform(landmark: PoseLandmark): Offset {
            var x = landmark.position.x * scaleX
            val y = landmark.position.y * scaleY

            if (isFrontCamera) {
                x = screenWidth - x
            }
            return Offset(x, y)
        }

        // 랜드마크 맵핑
        val landmarksMap = pose.allPoseLandmarks.associateBy({ it.landmarkType }, { transform(it) })

        // 뼈대(Line) 정의
        val connections = listOf(
            PoseLandmark.LEFT_SHOULDER to PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.LEFT_SHOULDER to PoseLandmark.LEFT_HIP,
            PoseLandmark.RIGHT_SHOULDER to PoseLandmark.RIGHT_HIP,
            PoseLandmark.LEFT_HIP to PoseLandmark.RIGHT_HIP,
            PoseLandmark.LEFT_HIP to PoseLandmark.LEFT_KNEE,
            PoseLandmark.LEFT_KNEE to PoseLandmark.LEFT_ANKLE,
            PoseLandmark.RIGHT_HIP to PoseLandmark.RIGHT_KNEE,
            PoseLandmark.RIGHT_KNEE to PoseLandmark.RIGHT_ANKLE,
            PoseLandmark.LEFT_SHOULDER to PoseLandmark.LEFT_ELBOW,
            PoseLandmark.LEFT_ELBOW to PoseLandmark.LEFT_WRIST,
            PoseLandmark.RIGHT_SHOULDER to PoseLandmark.RIGHT_ELBOW,
            PoseLandmark.RIGHT_ELBOW to PoseLandmark.RIGHT_WRIST
        )

        // 선 그리기
        connections.forEach { (start, end) ->
            val startPt = landmarksMap[start]
            val endPt = landmarksMap[end]
            if (startPt != null && endPt != null) {
                drawLine(Color.White.copy(alpha = 0.7f), startPt, endPt, strokeWidth = 8f)
            }
        }

        // 점 그리기 (관절)
        landmarksMap.forEach { (type, offset) ->
            // 주요 관절 강조 (엉덩이, 무릎, 발목)
            val isCoreJoint = type in setOf(
                PoseLandmark.LEFT_HIP, PoseLandmark.RIGHT_HIP,
                PoseLandmark.LEFT_KNEE, PoseLandmark.RIGHT_KNEE,
                PoseLandmark.LEFT_ANKLE, PoseLandmark.RIGHT_ANKLE
            )

            drawCircle(
                color = if (isCoreJoint) Color.Yellow else Color.Cyan,
                radius = if (isCoreJoint) 20f else 12f,
                center = offset
            )
        }
    }
}