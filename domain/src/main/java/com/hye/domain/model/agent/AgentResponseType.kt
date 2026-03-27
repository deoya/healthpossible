package com.hye.domain.model.agent

enum class AgentResponseType {
    CHAT,           // 단순 건강 조언 및 일상 대화
    CONFIRM_DELETE, // 작전 폐기(삭제) 재확인 요청
    CONFIRM_CHANGE, // 작전 교체 재확인 및 대안 제시
    RECOMMEND       // 주간 정기 추천 (5개 미션 하달)
}