package com.hye.shared.util

enum class DateFormatType(val pattern: String) {
    DEFAULT("yyyy-MM-dd HH:mm:ss"),

    KOREAN_DAY("M월 d일 (E)"),

    SIMPLE_TIME("a h:mm"),
}