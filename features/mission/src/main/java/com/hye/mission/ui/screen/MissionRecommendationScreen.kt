package com.hye.mission.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- Data Models ---
enum class Category(val label: String, val icon: ImageVector) {
    ALL("All", Icons.Default.List),
    EXERCISE("Exercise", Icons.Default.FitnessCenter),
    DIET("Diet", Icons.Default.Restaurant),
    DAILY("Daily", Icons.Default.WbSunny),
    RESTRICTION("Restriction", Icons.Default.Block)
}
//
//data class Mission(
//    val id: Int,
//    val category: Category,
//    val title: String,
//    val info: String, // Duration, Count, etc.
//    val frequency: String,
//    val reminder: String? = null,
//    val tags: List<String>,
//    val color: Color,
//    val containerColor: Color
//)
/*
// --- Sample Data ---
val sampleMissions = listOf(
    Mission(
        1, Category.EXERCISE, "아침 스트레칭", "10분", "매일", "07:00 AM",
        listOf("자세 교정 AI", "저강도"), Color(0xFF2563EB), Color(0xFFEFF6FF)
    ),
    Mission(
        2, Category.DIET, "식전 물 마시기", "3회/일", "매일", "식사 15분 전",
        listOf("수분 섭취", "알림"), Color(0xFF059669), Color(0xFFECFDF5)
    ),
    Mission(
        3, Category.DAILY, "독서 하기", "30분", "매일", "09:00 PM",
        listOf("웰니스", "집중"), Color(0xFFD97706), Color(0xFFFFFBEB)
    )
)

val aiRecommendations = listOf(
    Mission(
        4, Category.EXERCISE, "점심 산책", "3000보", "월-금", "12:30 PM",
        listOf("걸음 수", "야외"), Color(0xFF2563EB), Color(0xFFEFF6FF)
    ),
    Mission(
        5, Category.RESTRICTION, "야식 금지", "9시 이후", "매일", "08:50 PM",
        listOf("단식", "자기조절"), Color(0xFFE11D48), Color(0xFFFFF1F2)
    ),
    Mission(
        6, Category.EXERCISE, "코어 운동 기초", "15분", "주 3회", "유연함",
        listOf("자세 교정 AI", "초보자"), Color(0xFF2563EB), Color(0xFFEFF6FF)
    )
)

// --- Main Screen Composable ---

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionRecommendationScreen() {

    var selectedCategory by remember { mutableStateOf(Category.ALL) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "추천 미션",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )

                // Category Filter (LazyRow)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Category.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1E293B),
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color(0xFF64748B)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (selectedCategory == category) Color.Transparent else Color(0xFFE2E8F0),
                                enabled = true,
                                selected = selectedCategory == category
                            ),
                            shape = CircleShape
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Groups, contentDescription = null) },
                    label = { Text("커뮤니티") },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("홈") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2563EB),
                        selectedTextColor = Color(0xFF2563EB),
                        indicatorColor = Color(0xFFEFF6FF)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("마이페이지") },
                    selected = false,
                    onClick = { }
                )
            }
        },
        containerColor = Color(0xFFF8FAFC) // Slate 50 equivalent
    ) { innerPadding ->

        // Filter logic
        val filteredRecommended = if (selectedCategory == Category.ALL) sampleMissions else sampleMissions.filter { it.category == selectedCategory }
        val filteredAi = if (selectedCategory == Category.ALL) aiRecommendations else aiRecommendations.filter { it.category == selectedCategory }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Section 1: Recommended for You
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "회원님을 위한 추천",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        )
                        if (filteredRecommended.isNotEmpty()) {
                            Text(
                                text = "프로필 기반",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF2563EB),
                                modifier = Modifier
                                    .background(Color(0xFFEFF6FF), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (filteredRecommended.isEmpty()) {
                        EmptyStateMessage("이 카테고리에는 추천 미션이 없습니다.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            filteredRecommended.forEach { mission ->
                                MissionCard(mission)
                            }
                        }
                    }
                }
            }

            // Section 2: AI Recommendations
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF2563EB), Color(0xFF4F46E5))
                                    )
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AI",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI 추천",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        )
                    }

                    if (filteredAi.isEmpty()) {
                        EmptyStateMessage("AI 추천이 없습니다.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            filteredAi.forEach { mission ->
                                MissionCard(mission)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MissionCard(mission: Mission) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(mission.containerColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = mission.category.icon,
                            contentDescription = null,
                            tint = mission.color,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = mission.category.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = mission.color.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = mission.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                }
                IconButton(onClick = { /* Add mission */ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color(0xFFCBD5E1)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem2(Icons.Default.Schedule, mission.info)
                InfoItem2(Icons.Default.DateRange, mission.frequency)
                mission.reminder?.let {
                    InfoItem2(Icons.Default.Notifications, it)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tags
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mission.tags.forEach { tag ->
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF475569),
                        modifier = Modifier
                            .background(Color(0xFFF1F5F9), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem2(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color(0xFF94A3B8),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}*/