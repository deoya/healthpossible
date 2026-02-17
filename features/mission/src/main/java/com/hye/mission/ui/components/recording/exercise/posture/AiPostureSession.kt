package com.hye.mission.ui.components.recording.exercise.posture

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.ExerciseAgentType
import com.hye.mission.ui.components.ondevice.camera.PermissionDeniedContent
import com.hye.mission.ui.components.recording.layout.BaseBottomBarContentLayout
import com.hye.mission.ui.components.recording.layout.BaseSessionLayout
import com.hye.mission.ui.state.RecordState
import com.hye.shared.theme.AppTheme

//Todo: 이 화면에서는 가로모드 대응 해놓을 것
//Todo : 권한관리
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AiPostureSession(
    state: RecordState,
    onIncreaseCount: () -> Unit, // 카운트 증가용
    onToggleBottomSheet: (Boolean) -> Unit,
    onSelectExerciseType: (AiExerciseType) -> Unit,
    onUpdateFeedback: (String) -> Unit,
    onNavigateBack : () -> Unit
) {
    // 권한 체크
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        if (cameraPermissionState.status !is PermissionStatus.Granted) {
        }
    }

    BaseSessionLayout(
        backgroundContent = {
            when (cameraPermissionState.status) {
                // [Case A] 권한 허용됨 -> 카메라 미리보기 표시
                is PermissionStatus.Granted -> {
                    AiCameraContent(
                        state = state,
                        onRepCounted = onIncreaseCount,
                        onFeedback = onUpdateFeedback
                    )
                }

                // [Case B] 권한 거부됨 -> 안내 문구 및 요청 버튼 표시
                is PermissionStatus.Denied -> {
                    PermissionDeniedContent(
                        status = cameraPermissionState.status as PermissionStatus.Denied,
                        onRequestPermission = { cameraPermissionState.launchPermissionRequest() }
                    )
                }
            }
        },
        topBarContent = {
            // Todo: TopBar implementation
            Column(modifier = Modifier.padding(top = 48.dp, start = AppTheme.dimens.l)) {
                Surface(
                    color = AppTheme.colors.background,
                    shape = CircleShape,
                    shadowElevation = AppTheme.dimens.xxxxs,
                    modifier = Modifier.clickable { onNavigateBack() }
                ) {
                    Icon(Icons.Default.ArrowBack, null, modifier = Modifier.padding(AppTheme.dimens.xxs))
                }
            }

        },
        bottomBarContent = {
            BaseBottomBarContentLayout(
                content = {
                    AiPostureContent(
                        state = state,
                        onToggleBottomSheet = onToggleBottomSheet,
                    )
                },
                bottomSheetContent = {
                    AiPostureBottomSheet(
                        state = state,
                        progressText = state.progressLabel,
                        onToggleBottomSheet = onToggleBottomSheet,
                        onSelectExercise = onSelectExerciseType
                    )
                }
            )
        }
    )
}

@Preview
@Composable
fun PreviewExerciseSessionAi2() {
    AiPostureSession(
        state = RecordState(
            exerciseAgentType = ExerciseAgentType.AI_POSTURE,
            feedbackMessage = "허리를 조금 더 펴주세요!"
        ),
        onToggleBottomSheet = {},
        onSelectExerciseType = {},
        onIncreaseCount = {},
        onUpdateFeedback = {},
        onNavigateBack = {}
    )
}


