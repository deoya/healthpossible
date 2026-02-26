package com.hye.domain.model.survey

data class SurveyQuestion(
    val id: String, // 질문을 식별하는 고유 ID
    val tag: String,
    val title: String,
    val options: List<String>,
    val isMultiSelect: Boolean = false,
    val exclusiveOption: String? = null // 선택 시 다른 항목을 모두 해제해야 하는 옵션 (예: "없음")
)