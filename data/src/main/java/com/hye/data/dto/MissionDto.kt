package com.hye.data.dto

import com.google.firebase.firestore.PropertyName

data class MissionDto(
    @get:PropertyName(UID)
    @set:PropertyName(UID)
    var uid: String = "",

    // --- 공통 필드 ---
    @get:PropertyName(ID)
    @set:PropertyName(ID)
    var id: String = "",

    @get:PropertyName(TITLE)
    @set:PropertyName(TITLE)
    var title: String = "",

    @get:PropertyName(TYPE)
    @set:PropertyName(TYPE)
    var type: String = "",

    @get:PropertyName(WEEKLY_TARGET_COUNT)
    @set:PropertyName(WEEKLY_TARGET_COUNT)
    var weeklyTargetCount: Int = 0,

    @get:PropertyName(WEEK_IDENTIFIER)
    @set:PropertyName(WEEK_IDENTIFIER)
    var weekIdentifier : String? = null,

    @get:PropertyName(IS_TEMPLATE)
    @set:PropertyName(IS_TEMPLATE)
    var isTemplate: Boolean = false,




    @get:PropertyName(NOTIFICATION_TIME)
    @set:PropertyName(NOTIFICATION_TIME)
    var notificationTime: String? = null,

    @get:PropertyName(MEMO)
    @set:PropertyName(MEMO)
    var memo: String? = null,

    // --- 운동 미션 필드 ---
    @get:PropertyName(UNIT)
    @set:PropertyName(UNIT)
    var unit: String? = null,

    @get:PropertyName(SELECTED_EXERCISE)
    @set:PropertyName(SELECTED_EXERCISE)
    var selectedExercise: String? = null,

    @get:PropertyName(TARGET_VALUE)
    @set:PropertyName(TARGET_VALUE)
    var targetValue: Int? = null,

    @get:PropertyName(USE_SUPPORT_AGENT)
    @set:PropertyName(USE_SUPPORT_AGENT)
    var useSupportAgent: Boolean? = null,

    // --- 식단 미션 필드 ---
    @get:PropertyName(RECORD_METHOD)
    @set:PropertyName(RECORD_METHOD)
    var recordMethod: String? = null,

    // --- 루틴 미션 필드 ---
    @get:PropertyName(DAILY_TARGET_AMOUNT)
    @set:PropertyName(DAILY_TARGET_AMOUNT)
    var dailyTargetAmount: Int? = null,

    @get:PropertyName(AMOUNT_PER_STEP)
    @set:PropertyName(AMOUNT_PER_STEP)
    var amountPerStep: Int? = null,

    @get:PropertyName(UNIT_LABEL)
    @set:PropertyName(UNIT_LABEL)
    var unitLabel: String? = null,

    // --- 제한 미션 필드 ---
    @get:PropertyName(RESTRICTION_TYPE)
    @set:PropertyName(RESTRICTION_TYPE)
    var restrictionType: String? = null,

    @get:PropertyName(MAX_ALLOWED_MINUTES)
    @set:PropertyName(MAX_ALLOWED_MINUTES)
    var maxAllowedMinutes: Int? = null
) {
    companion object Fields {
        const val UID = "uid"
        const val ID = "id"
        const val TITLE = "title"
        const val TYPE = "type"
        const val WEEKLY_TARGET_COUNT = "weekly_target_count"
        const val WEEK_IDENTIFIER = "week_identifier"
        const val IS_TEMPLATE = "is_template"

        const val NOTIFICATION_TIME = "notification_time"
        const val MEMO = "memo"

        const val UNIT = "unit"
        const val SELECTED_EXERCISE = "selected_exercise"
        const val TARGET_VALUE = "target_value"
        const val USE_SUPPORT_AGENT = "use_support_agent"

        const val RECORD_METHOD = "record_method"

        const val DAILY_TARGET_AMOUNT = "daily_target_amount"
        const val AMOUNT_PER_STEP = "amount_per_step"
        const val UNIT_LABEL = "unit_label"

        const val RESTRICTION_TYPE = "restriction_type"
        const val MAX_ALLOWED_MINUTES = "max_allowed_minutes"
    }
}
