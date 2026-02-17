package com.hye.mission.ui.util

import android.content.Context
import com.hye.domain.model.mission.feedback.AgentMessageSet
import com.hye.domain.model.mission.types.AiExerciseType
import com.hye.features.mission.R


class AgentMessageProvider (private val context: Context) {

    val missing:String=context.getString(R.string.mission_agent_missing)
    val identifying:String=context.getString(R.string.mission_status_identifying)
    val ready:String=context.getString(R.string.mission_status_ready)
    val standStraight:String=context.getString(R.string.mission_error_stand_straight)
    val returnToScreen:String=context.getString(R.string.mission_error_return_to_screen)
    val climax:String=context.getString(R.string.mission_climax_alert)
    val successGeneric:List<String> = context.resources.getStringArray(R.array.mission_success_generic).toList()
    val lastOne:String=context.getString(R.string.mission_success_last_one)
    val complete:String=context.getString(R.string.mission_success_complete)

    fun getExerciseSet(type: AiExerciseType): AgentMessageSet = when (type) {
            AiExerciseType.SQUAT -> AgentMessageSet(
                descentNormal = context.resources.getStringArray(R.array.squat_descent_normal).toList(),
                descentWarning = context.resources.getStringArray(R.array.squat_descent_warning).toList(),
                bottom = context.resources.getStringArray(R.array.squat_bottom).toList(),
                ascent = context.resources.getStringArray(R.array.squat_ascent).toList(),
                errorKnee = context.resources.getStringArray(R.array.squat_error_knee).toList(),
                errorBack = context.resources.getStringArray(R.array.squat_error_back).toList()
            )
            AiExerciseType.LUNGE -> AgentMessageSet(
                descentNormal = context.resources.getStringArray(R.array.lunge_descent_normal).toList(),
                descentWarning = context.resources.getStringArray(R.array.lunge_descent_warning).toList(),
                bottom = context.resources.getStringArray(R.array.lunge_bottom).toList(),
                ascent = context.resources.getStringArray(R.array.lunge_ascent).toList(),
                errorKnee = emptyList(),
                errorBack = context.resources.getStringArray(R.array.squat_error_back).toList()
            )
            else -> AgentMessageSet(
                descentNormal = emptyList(), descentWarning = emptyList(), bottom = emptyList(),
                ascent = emptyList(), errorKnee = emptyList(), errorBack = emptyList()
            )
        }
    fun report(count:Int): String = context.resources.getString(R.string.mission_success_count_report,count)
    fun midReport(count:Int): String = context.resources.getString(R.string.mission_success_mid_check,count)

}