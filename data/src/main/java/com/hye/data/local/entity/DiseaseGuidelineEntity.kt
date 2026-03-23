package com.hye.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disease_guideline_table")
data class DiseaseGuidelineEntity(
    @PrimaryKey
    val diseaseName: String,         // 예: "당뇨병" (프로필에 저장된 이름과 동일하게 매칭)
    val guidelineContent: String,    // AI가 요약해준 JSON 형식의 텍스트 또는 정제된 지침 내용
    val updatedAt: Long              // 캐시 갱신 주기를 관리하기 위한 저장 시간 (타임스탬프)
)