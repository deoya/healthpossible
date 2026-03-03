package com.hye.mission.ui.state

import com.hye.domain.model.mission.types.Mission

data class RecommendUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val recommendations: List<Mission> = emptyList()
)