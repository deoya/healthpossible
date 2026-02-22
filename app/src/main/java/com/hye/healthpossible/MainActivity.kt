package com.hye.healthpossible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hye.healthpossible.ui.MainViewModel
import com.hye.healthpossible.ui.screen.EntryPoint
import com.hye.healthpossible.ui.screen.OnboardingScreen
import com.hye.healthpossible.ui.screen.SplashScreen
import com.hye.shared.theme.HPAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: MainViewModel by viewModels()

        setContent {
            HPAppTheme {
                val uiState by viewModel.uiStatus.collectAsStateWithLifecycle()

                Crossfade(targetState = uiState.isSplashLoading, label = "Splash") { isLoading ->
                    if (isLoading) {
                        SplashScreen (
                            onSplashFinished = {
                                viewModel.onSplashFinished()
                            }
                        )
                    } else {
                        EntryPoint(uiState.isLoggedIn)
                    }
                }

            }
        }
    }
}
