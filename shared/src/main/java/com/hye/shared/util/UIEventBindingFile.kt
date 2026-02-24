package com.hye.shared.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object ClickGuard {
    @OptIn(ExperimentalAtomicApi::class)
    @Composable
    fun RememberSafeClick(
        intervalTime: Long = 300L,
        onClick: () -> Unit,
    ): () -> Unit {

        val currentOnClick by rememberUpdatedState(onClick)
        val lastClickTime = remember { AtomicLong(0L) }

        return remember(intervalTime) {
            {
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastClickTime.load() > intervalTime) {
                    lastClickTime.store(currentTime)
                    currentOnClick()
                }
            }
        }
    }
}

fun Modifier.clickableSingle(
    intervalTime: Long = 300L,
    onClick: () -> Unit,
): Modifier = composed {
    val debouncedClick = ClickGuard.RememberSafeClick(
        intervalTime = intervalTime,
        onClick = onClick
    )

    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
    ) {
        debouncedClick()
    }
}