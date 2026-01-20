package com.hye.mission.ui.components.mission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hye.features.mission.R
import com.hye.mission.ui.util.dailyProgressCardStyle
import com.hye.shared.components.ui.StyledCard
import com.hye.shared.theme.AppTheme

@Composable
fun DailyProgressCard() {
    val currentDate = "11월 24일 (일)" // 실제 앱에선 LocalDate 사용
    val progress = 0.35f // 35% 완료
    val cheerMessages = stringArrayResource(id = R.array.mission_cheer_messages)
    val randomMessage = cheerMessages[1]

    StyledCard(dailyProgressCardStyle)  {
        Row(
            modifier = Modifier.padding(AppTheme.dimens.doubleExtraLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)
            ) {
                Text(
                    text = randomMessage,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${(progress * 100).toInt()}% 달성 중",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "5개 중 2개 완료",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Circular Progress Indicator
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(80.dp),
                    color = Color.White.copy(alpha = 0.3f),
                    strokeWidth = 8.dp,
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(80.dp),
                    color = Color.White,
                    strokeWidth = 8.dp,
                    trackColor = Color.Transparent,
                    strokeCap = StrokeCap.Round
                )
                Icon(
                    painter = painterResource(R.drawable.wireless),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}
