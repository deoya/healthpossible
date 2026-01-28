package com.hye.domain.usecase.mission

import com.hye.domain.repository.MissionRepository
import javax.inject.Inject

class GetMissionRecordsUseCase @Inject constructor(
    private val repository: MissionRepository
) {
    operator fun invoke(date: String) = repository.getMissionRecords(date)
}