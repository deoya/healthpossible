package com.hye.data.common

fun cleanHtmlAndCdata(rawText: String): String {
    return rawText
        // 1. CDATA 태그 자체를 제거
        .replace("<!\\[CDATA\\[".toRegex(), "")
        .replace("]]>".toRegex(), "")
        // 2. <p>, <br>, <img>, <quillbot...> 등 모든 HTML 태그 제거
        .replace(Regex("<[^>]*>"), "")
        // 3. 특수 공백 문자 변환
        .replace("&nbsp;", " ")
        // 4. 불필요하게 여러 줄로 띄워진 공백을 한 줄로 압축
        .replace("\\n\\s*\\n".toRegex(), "\n")
        .trim()
}