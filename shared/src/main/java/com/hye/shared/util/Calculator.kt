package com.hye.shared.util

object Calculator {
    val progress : (Int, Int) -> Float = {a,b ->
        when{
            (a > 0) -> b.toFloat() / a
            else -> 0f
        }
    }
}