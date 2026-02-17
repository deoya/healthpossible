package com.hye.mission.ui.analyzer

import kotlin.random.Random

fun AiPoseAnalyzer.sendFeedback(msg: String, force: Boolean = false, keepDuration: Long = 0) {
    val currentTime = System.currentTimeMillis()
    if (force || (currentTime > messageBlockTimestamp && currentTime - lastFeedbackTime > 1200)) {
        onFeedback(msg)
        lastFeedbackTime = currentTime
        if (keepDuration > 0) messageBlockTimestamp = currentTime + keepDuration
    }
}

// 확률 기반 메시지 선택 (70:20:10 법칙)
fun AiPoseAnalyzer.getDescentMessage(): String {
    val roll = Random.nextFloat()
    return when {
        roll < 0.7 -> typeMessages.descentNormal.random()
        roll < 0.9 -> typeMessages.descentWarning.random()
        else -> agentMessage.climax
    }
}

fun AiPoseAnalyzer.getSuccessMessage(count: Int, total: Int): String {
    return when {
        count == total - 1 -> agentMessage.lastOne
        count == total -> agentMessage.complete
        count % 3 == 0 -> agentMessage.midReport(count)
        else -> {
            if (Random.nextBoolean()) agentMessage.successGeneric.random()
            else agentMessage.report(count)
        }
    }
}