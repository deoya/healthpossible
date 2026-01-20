package com.hye.mission.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.domain.usecase.mission.MissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissionViewModel @Inject constructor(
    private val missionUseCase: MissionUseCase
) : ViewModel(){
    private val _uiStatus = MutableStateFlow(MissionState())
    val uiStatus = _uiStatus.asStateFlow()

    init {
        getMissionList()
    }

    private fun getMissionList() {
        viewModelScope.launch {
            missionUseCase.getMissionList().collectLatest { result ->
                _uiStatus.update { it.copy(missionList = result) }
            }
        }
    }
}