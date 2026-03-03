package com.hye.shared.util

import android.annotation.SuppressLint
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@SuppressLint("NewApi")
fun getCurrentSeoulZonedDateTime(): ZonedDateTime {
    val seoulZone = ZoneId.of("Asia/Seoul")
    return ZonedDateTime.ofInstant(Instant.now(), seoulZone)
}

@SuppressLint("NewApi")
fun getCurrentSeoulDate(): LocalDate {
    return getCurrentSeoulZonedDateTime().toLocalDate()
}

@SuppressLint("NewApi")
fun getCurrentSeoulTime(): LocalTime {
    return getCurrentSeoulZonedDateTime().toLocalTime()
}

@SuppressLint("NewApi")
fun getCurrentFormattedTime(type: DateFormatType = DateFormatType.DEFAULT): String {
    val formatter = DateTimeFormatter.ofPattern(type.pattern)
    return getCurrentSeoulZonedDateTime().format(formatter)
}