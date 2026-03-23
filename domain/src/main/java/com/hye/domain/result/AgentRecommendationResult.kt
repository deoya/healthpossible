package com.hye.domain.result

sealed class AgentRecommendationResult<out T> {
    data class Success<T>(
        val data: T
    ) : AgentRecommendationResult<T>()

    data class Error(
        val exception: Throwable,
        val fallbackBriefing: String // 통신 실패 시 로컬에서 사용할 대체 브리핑 대사
    ) : AgentRecommendationResult<Nothing>()
}
