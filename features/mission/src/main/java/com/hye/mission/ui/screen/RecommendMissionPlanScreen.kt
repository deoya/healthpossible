package com.hye.mission.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

// 더미 데이터 모델
data class MissionData(
    val id: String,
    val title: String,
    val type: String, // 운동, 식단 등
    val time: Int,
    val days: Int,
    val tags: List<String>,
    val description: String,
    val activeUsers: List<Color> = emptyList() // 현재 사용중인 유저 프사(여기선 컬러로 대체)
)

@Preview
@Composable
fun RecommendMissionPlanScreen() {
    val scrollState = rememberScrollState()

    // 샘플 데이터
    val missions = listOf(
        MissionData("1", "미션명 1", "운동", 200, 3, listOf("알림", "월, 화, 수"), "이 미션은 어쩌구 저쩌구 자세한 설명이 들어갑니다.", activeUsers = listOf(Color.Red, Color.Blue, Color.Green)),
        MissionData("2", "미션명 2", "식단", 200, 3, listOf("알림", "월, 화, 수"), "식단 관리는 중요합니다. 어쩌구 저쩌구.", activeUsers = emptyList()),
        MissionData("3", "미션명 3", "상시", 100, 5, listOf("알림", "매일"), "상시 미션 설명입니다.", activeUsers = listOf(Color.Yellow))
    )

    Scaffold(
        topBar = {
            Column {
                // 1. 뒤로가기 및 필터 영역
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    // 필터 칩 (가로 스크롤)
                    val filters = listOf("전체보기", "운동", "식단", "상시", "제한")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filters) { filter ->
                            FilterChipItem(filter, isSelected = filter == "전체보기")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            // 2. 닉네임을 위한 추천 섹션
            Text(
                text = "닉네임을 위한 추천",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // 2열 그리드 배치 (Row 안에 2개씩 넣기)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FlippableMissionCard(missions[0])
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FlippableMissionCard(missions[1])
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. 오늘의 랜덤 추천 섹션
            Text(
                text = "오늘의 랜덤 추천",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 랜덤 추천은 꽉 찬 화면으로 하나 보여줌
            FlippableMissionCard(missions[2], modifier = Modifier.fillMaxWidth().height(250.dp))

            Spacer(modifier = Modifier.height(50.dp)) // 하단 여백
        }
    }
}

// ------------------------------------
// [핵심] 뒤집히는 카드 컴포넌트
// ------------------------------------
@Composable
fun FlippableMissionCard(
    mission: MissionData,
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    // 회전 애니메이션 값 (0도 <-> 180도)
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "flip"
    )

    Box(
        modifier = modifier
            .height(280.dp) // 카드 기본 높이
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density // 3D 원근감 효과
            }
            .clickable { isFlipped = !isFlipped }, // 클릭 시 뒤집기
        contentAlignment = Alignment.Center
    ) {
        // 90도 이전이면 앞면, 90도 이후면 뒷면 렌더링
        if (rotation <= 90f) {
            MissionCardFront(mission)
        } else {
            // 뒷면은 텍스트가 반대로 보이지 않게 다시 180도 돌려줌
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                MissionCardBack(mission)
            }
        }
    }
}

// [앞면] 미션 정보 (아이콘, 시간, 알림 등)
@Composable
fun MissionCardFront(mission: MissionData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF3F6)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 아이콘 (운동/식단 등)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF6C757D), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (mission.type == "운동") Icons.Default.FitnessCenter else Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 미션 제목
            Text(
                text = mission.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 정보 행 (시간, 날짜, 알림)
            InfoRow(Icons.Default.Timer, "${mission.time}")
            InfoRow(Icons.Default.CalendarToday, "${mission.days}")
            InfoRow(Icons.Default.Notifications, "", isIconOnly = true)

            Spacer(modifier = Modifier.weight(1f))

            // 태그 (알림, 요일)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mission.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFDDE1E6), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = tag, fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

// [뒷면] 상세 설명 + (조건부) 유저 프사
@Composable
fun MissionCardBack(mission: MissionData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Color(0xFFEFF3F6), RoundedCornerShape(16.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 설명 텍스트
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "미션 설명",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mission.description,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    lineHeight = 24.sp
                )
            }

            // 하단 영역 (프사 + 추가 버튼)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 우측 하단 정렬
                    .fillMaxWidth()
                    .padding(start = 16.dp), // 왼쪽 패딩
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // [조건부 렌더링] 사용하는 유저가 있을 때만 프사 표시
                if (mission.activeUsers.isNotEmpty()) {
                    UserAvatarPile(users = mission.activeUsers)
                } else {
                    Spacer(modifier = Modifier.width(1.dp)) // 비어있을 때 공간 차지용
                }

                // 추가 버튼 (모서리 둥글게 깎인 사각형)
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFAAB8C2),
                            shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .clickable { /* 추가 로직 */ }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("추가", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

// 유저 프사 겹쳐서 보여주기 (Avatar Pile)
@Composable
fun UserAvatarPile(users: List<Color>) {
    Row(
        modifier = Modifier
            .padding(bottom = 12.dp) // 버튼 높이와 맞추기 위해 살짝 띄움
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        users.take(3).forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .offset(x = (index * -10).dp) // 왼쪽으로 조금씩 겹치게 이동
                    .size(30.dp)
                    .border(2.dp, Color.White, CircleShape) // 테두리 흰색으로 구분
                    .background(color, CircleShape)
                    .zIndex((3 - index).toFloat()) // 앞쪽 순서대로 위로 오게
            )
        }
        // "+1" 같은 숫자 표시 (필요시 추가)
        if (users.size > 3) {
            Box(
                modifier = Modifier
                    .offset(x = (-30).dp)
                    .size(30.dp)
                    .background(Color.LightGray, CircleShape)
                    .zIndex(0f),
                contentAlignment = Alignment.Center
            ) {
                Text("+${users.size - 3}", fontSize = 10.sp, color = Color.White)
            }
        }
    }
}

// 작은 정보 행 (아이콘 + 텍스트)
@Composable
fun InfoRow(icon: ImageVector, text: String, isIconOnly: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
        if (!isIconOnly) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

// 상단 필터 칩 스타일
@Composable
fun FilterChipItem(text: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) Color(0xFF333333) else Color(0xFFF0F0F0),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}