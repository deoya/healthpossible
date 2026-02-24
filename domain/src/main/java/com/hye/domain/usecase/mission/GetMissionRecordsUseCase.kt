package com.hye.domain.usecase.mission

import com.hye.domain.repository.MissionRepository
import java.time.LocalDate
import javax.inject.Inject

class GetMissionRecordsUseCase @Inject constructor(
    private val repository: MissionRepository
) {
    operator fun invoke(date: LocalDate) = repository.getMissionRecords(date)
}