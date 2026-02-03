package com.hye.shared.util

object Calculator {
    fun progress(total: Int, current: Int)  :  Float =
        when{
            (total > 0) -> current.toFloat() / total.toFloat()
            else -> 0f
        }

    fun progress(total: String, current: String) : Float {
        val totalSec = total.toSeconds()
        val currentSec = current.toSeconds()
        return when{
            (totalSec > 0) -> currentSec.toFloat() / totalSec.toFloat()
            else -> 0f
        }
    }
}

private fun String.toSeconds(): Int {
    if (this.isBlank()) return 0

    val parts = this.split(":")

    return when (parts.size) {
        2 -> {
            val min = parts[0].toIntOrNull() ?: 0
            val sec = parts[1].toIntOrNull() ?: 0
            (min * 60) + sec
        }
        else -> 0
    }
}