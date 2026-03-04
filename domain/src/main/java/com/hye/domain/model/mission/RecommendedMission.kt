package com.hye.domain.model.mission

import com.hye.domain.model.mission.types.Mission

enum class Suitability(val label: String) {
    HIGH("상"),
    MEDIUM("중"),
    LOW("하")
}

data class RecommendedMission(
    val mission: Mission,
    val suitability: Suitability
)