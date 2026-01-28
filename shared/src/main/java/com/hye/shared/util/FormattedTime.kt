package com.hye.shared.util

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@SuppressLint("NewApi")
fun getCurrentFormattedTime(type: DateFormatType = DateFormatType.DEFAULT): String {
    // 1. 한국 시간대 정의
    val seoulZone = ZoneId.of("Asia/Seoul")

    // 2. 현재 시간을 한국 시간대로 변환
    // Date 대신 Instant.now()를 사용함
    val nowInSeoul = ZonedDateTime.ofInstant(Instant.now(), seoulZone)

    // 3. 서버 표준 포맷 정의
    val formatter = DateTimeFormatter.ofPattern(type.pattern)

    // 4. 포맷 적용 및 반환
    return nowInSeoul.format(formatter)
}
