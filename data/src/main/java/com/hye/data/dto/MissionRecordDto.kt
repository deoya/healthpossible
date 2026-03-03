package com.hye.data.dto

import com.google.firebase.firestore.PropertyName

data class MissionRecordDto(

    @get:PropertyName(UID) @set:PropertyName(UID)
    var uid: String = "",

    @get:PropertyName(ID) @set:PropertyName(ID)
    var id: String = "",

    @get:PropertyName(MISSION_ID) @set:PropertyName(MISSION_ID)
    var missionId: String = "",

    @get:PropertyName(DATE) @set:PropertyName(DATE)
    var date: String = "", // "YYYY-MM-DD"

    @get:PropertyName(PROGRESS) @set:PropertyName(PROGRESS)
    var progress: Int = 0,

    @get:PropertyName(IS_COMPLETED) @set:PropertyName(IS_COMPLETED)
    var isCompleted: Boolean = false,

    @get:PropertyName(COMPLETED_AT) @set:PropertyName(COMPLETED_AT)
    var completedAt: String? = null // "HH:mm:ss"
) {
    companion object Fields {
        const val UID = "uid"
        const val ID = "id"
        const val MISSION_ID = "missionId"
        const val DATE = "date"
        const val PROGRESS = "progress"
        const val IS_COMPLETED = "isCompleted"
        const val COMPLETED_AT = "completedAt"
    }
}
