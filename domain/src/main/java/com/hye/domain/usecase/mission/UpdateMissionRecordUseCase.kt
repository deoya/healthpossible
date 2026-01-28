package com.hye.domain.usecase.mission

import com.hye.domain.model.mission.MissionRecord
import com.hye.domain.repository.MissionRepository
import javax.inject.Inject

class UpdateMissionRecordUseCase @Inject constructor(
    private val repository: MissionRepository
) {
    operator fun invoke(record: MissionRecord) = repository.updateMissionRecord(record)
}