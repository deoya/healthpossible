package com.hye.domain.model.mission.feedback

data class AgentMessageSet(
    val descentNormal: List<String>,
    val descentWarning: List<String>,
    val bottom: List<String>,
    val ascent: List<String>,
    val errorKnee: List<String>, // 무릎 에러
    val errorBack: List<String>  // 허리/상체 에러 (New)
)