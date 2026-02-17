package com.hye.mission.ui.components.recording.exercise.posture

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.domain.model.mission.types.AiSessionMode
import com.hye.features.mission.R
import com.hye.mission.ui.analyzer.AiPoseAnalyzer
import com.hye.mission.ui.state.RecordState
import com.hye.shared.util.text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import timber.log.Timber

@Composable
fun AiCameraContent(
    state: RecordState,
    onRepCounted: () -> Unit,
    onFeedback: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val tagName = R.string.mission_ai_camera_content.text
    val aiPoseAnalyzerTagName = context.getString(R.string.mission_ai_pose_analyzer)


    var detectedPose by remember { mutableStateOf<Pose?>(null) }
    var imageWidth by remember { mutableIntStateOf(0) }
    var imageHeight by remember { mutableIntStateOf(0) }

    val poseDetector = remember {
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()
        PoseDetection.getClient(options)
    }

    val currentExercise = (state.sessionMode as? AiSessionMode.AiRepMode)?.exerciseType
        ?: AiExerciseType.SQUAT

    val cameraController = remember { LifecycleCameraController(context) }
    val executor = remember { Dispatchers.Default.limitedParallelism(1).asExecutor() }

    LaunchedEffect(cameraController, currentExercise) {
        Timber.tag(tagName).d("Starting camera session for $currentExercise")
        try {
            cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            cameraController.bindToLifecycle(lifecycleOwner)

            Timber.tag(tagName).d("분석기 시작")

            cameraController.setImageAnalysisAnalyzer(
                executor,
                AiPoseAnalyzer(
                    context = context,
                    aiPoseAnalyzerTagName= aiPoseAnalyzerTagName,
                    exerciseType = currentExercise,
                    poseDetector = poseDetector,
                    onRepCounted = {
                        Timber.tag(tagName).i("Rep Counted! (Current: $currentExercise)")
                        onRepCounted() },
                    onFeedback = onFeedback,
                    onPoseDetected = { pose, w, h ->
                        detectedPose = pose
                        imageWidth = w
                        imageHeight = h
                    },
                )
            )
        }catch (e:Exception){
            Timber.tag(tagName).e(e, "Failed to bind camera or analyzer")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 카메라 미리보기
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    this.controller = cameraController
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        detectedPose?.let { pose ->
            if (imageWidth > 0 && imageHeight > 0) {
                PoseOverlay(
                    pose = pose,
                    imageWidth = imageWidth,
                    imageHeight = imageHeight,
                    isFrontCamera = true,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}