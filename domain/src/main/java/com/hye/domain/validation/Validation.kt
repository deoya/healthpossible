package com.hye.domain.validation

object Validation {
    const val ALLOWED_CHAR_PATTERN = "^[a-zA-Z0-9가-힣]"
    internal fun buildRegex(vararg patterns:String) : Regex{
        return Regex(patterns.joinToString("")+"$" )
    }
    fun makeRegexWithLength(basePattern: String, min: Int, max: Int): Regex {
        return buildRegex(basePattern, "{${min},${max}}")
    }
}

object Validator{
    fun Required(value: String): String? = if (value.isEmpty()) "필수 항목입니다." else null

    fun MinLength(value: String, min: Int): String? = if (value.length < min) "${min}글자 이상 입력해주세요." else null

    fun NotContainSpace(value: String): String? = if (value.contains(" ")) "공백은 포함할 수 없습니다." else null

    fun AllowedChar(value: String): String? = if (!value.matches(Validation.buildRegex(Validation.ALLOWED_CHAR_PATTERN,"+"))) "한글, 영문, 숫자만 사용할 수 있습니다." else null

    fun NotContainReservedWord(value: String, reservedWords: List<String>): String? = if (reservedWords.any { value.contains(it, ignoreCase = true) }) "사용할 수 없는 단어가 포함되어 있습니다." else null
}
