package com.hye.mission.ui.components.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hye.domain.model.mission.Mission
import com.hye.mission.ui.util.UserMissionCardStyle
import com.hye.mission.ui.util.getIconDesign
import com.hye.shared.components.ui.StyledCard


@Composable
fun UserMissionCard(mission: Mission, onClick: () -> Unit = {}) {
    val (color, containerColor, icon) = mission.getIconDesign()

    val isCompleted = false

    StyledCard(
        style = UserMissionCardStyle(isCompleted, 1f)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아이콘 박스
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 정보 텍스트
            Column(modifier = Modifier.weight(1f)) {
                // 태그 표시
                if (mission.tags.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        mission.tags.take(2).forEach { tag ->
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 제목
                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

            }
        }
    }
}
/*
    val missionStatus = remember(mission.status) {
        when (mission.status) {
            "COMPLETED" -> MissionState.COMPLETED
            "IN_PROGRESS" -> MissionStatus.IN_PROGRESS
            else -> MissionStatus.PENDING
        }
    }
    val isCompleted = missionStatus== MissionStatus.COMPLETED
    val isInProgress = missionStatus == MissionStatus.IN_PROGRESS
    // 카드 배경색 및 테두리 애니메이션
    val cardAlpha = if (isCompleted) 0.6f else 1f

    StyledCard (
        style = UserMissionCardStyle(isCompleted,cardAlpha)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Icon Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isCompleted) Color(0xFFE2E8F0) else mission.containerColor),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = mission.category.getIconDesign().icon,
                        contentDescription = null,
                        tint = mission.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Time Badge
                    Surface(
                        color = BgColor,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = mission.timeSlot,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (isInProgress) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "진행 중",
                            style = MaterialTheme.typography.labelSmall,
                            color = HealthBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) TextSecondary else TextPrimary,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${mission.progress} / ${mission.target}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            // 3. Action Button
            if (!isCompleted) {

                StartButton(
                    isInProgress = isInProgress,
                    onClick = { /* Start Logic */ }
                )
            }
        }

        // Progress Bar (Only for In Progress)
        if (isInProgress) {
            LinearProgressIndicator(
                progress = { 0.5f }, // 예시: 50%
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = HealthBlue,
                trackColor = HealthBlueLight
            )
        }
    }*/
