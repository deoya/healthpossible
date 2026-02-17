package com.hye.shared.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hye.shared.theme.AppTheme
import com.hye.shared.util.ObserveBaseEffects
import timber.log.Timber

@Composable
fun BaseScreenTemplate(
    viewModel: BaseViewModel,
    screenName: String? = null,

    isLoading: Boolean,
    errorMessage: String? = null,

    setTopBarActions: ((@Composable () -> Unit)?) -> Unit = {},
    topBarActionContent: @Composable () -> Unit = {},
    onNavigateBack: () -> Unit = {},

    loadingContent: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AppTheme.colors.mainColor)
        }
    },
    errorContent: @Composable (String) -> Unit = { msg ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "오류가 발생했습니다: $msg",
                color = AppTheme.colors.error,
            )
        }
    },
    content: @Composable () -> Unit
) {
    LaunchedEffect(Unit) {
        Timber.tag("Screen").d("$screenName Entered")
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Timber.tag("Screen").e("$screenName Error State -> $it")
        }
    }

    ObserveBaseEffects(
        viewModel = viewModel,
        onNavigateBack = onNavigateBack
    )

    LaunchedEffect(topBarActionContent) {
        setTopBarActions(topBarActionContent)
    }

    when {
        isLoading -> loadingContent()
        errorMessage != null -> errorContent(errorMessage)
        else -> content()
    }
}