package com.hye.shared.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.shared.util.CommonUiEffect
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel : ViewModel() {
    // 1. 공통 이펙트 채널
    private val _commonEffect = Channel<CommonUiEffect>()
    val commonEffect = _commonEffect.receiveAsFlow()

    // 2. 공통 에러 핸들러 (Timber + Toast)
    protected val commonCeh = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "CRITICAL: BaseViewModel Catch")
        showToast("일시적인 오류가 발생했습니다.")
    }

    protected fun showToast(message: String) {
        viewModelScope.launch {
            _commonEffect.send(CommonUiEffect.ShowToast(message))
        }
    }

    protected fun navigateBack() {
        viewModelScope.launch {
            _commonEffect.send(CommonUiEffect.NavigateBack)
        }
    }
}