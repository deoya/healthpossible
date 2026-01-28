package com.hye.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

val Int.text: String
    @Composable get() = stringResource(id = this)

@Composable
fun Int.text(vararg args: Any): String = stringResource(id = this, formatArgs = args)
