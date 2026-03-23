package com.hye.domain.model.survey

data class SurveyQuestion(
    val id: SurveyQuestionId, // 질문을 식별하는 고유 ID
    val tag: String,
    val title: String,
    val options: List<String>,
    val isMultiSelect: Boolean = false,
    val exclusiveOption: String? = null // 선택 시 다른 항목을 모두 해제해야 하는 옵션 (예: "없음")
)

enum class SurveyQuestionId(val id: String) {
    HEALTH_GOAL("Q1"),   // 건강 목표
    PAIN_POINT("Q2"),    // 통증 부위
    BAD_HABIT("Q3"),     // 나쁜 습관
    ACTIVITY_LEVEL("Q4"), // 활동 수준
    CHRONIC_DISEASE("Q5") // 주의할 만성질환
}