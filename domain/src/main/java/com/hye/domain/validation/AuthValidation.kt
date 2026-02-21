package com.hye.domain.validation

/**
 * 코드네임 정책 정의
 */
object CodeNamePolicy {
    const val CODENAME_MIN_LEN = 2
    const val CODENAME_MAX_LEN = 10
}

/**
 * 인증 관련 금칙어 목록
 */
object AuthValidation {
    //Todo: 욕설, 비방 등 금지 llm 추가
    val reservedWords = listOf("운영자", "관리자", "어드민", "admin", "healthposable", "헬스포저블")
}
