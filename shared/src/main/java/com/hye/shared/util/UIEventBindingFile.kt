package com.hye.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi

//더블 클릭" 또는 "광클"을 방지
@OptIn(ExperimentalAtomicApi::class)
@Composable
fun rememberSeSACThrottledOnClick(
    intervalTime: Long = 300L,// 1. 중복 클릭 방지 시간 (기본 0.3초)
    onClick: () -> Unit
): () -> Unit {// 2. 새로운 클릭 람다 함수를 반환

    // 3. onClick이 바뀌어도 람다 객체를 새로 만들지 않도록 상태로 관리
    // onClick이 바뀌어도 람다 객체를 새로 만들지 않도록 상태로 관리
    val currentOnClick by rememberUpdatedState(onClick)
// 4. 마지막 클릭 시간을 저장할 변수 (스레드 안전성 확보)
    val lastClickTime = remember { AtomicLong(0L) }

    // 5. 람다 자체를 캐싱 (intervalTime 바뀔 때만 재생성)
    //람다 자체를 캐싱 (intervalTime 바뀔 때만 재생성)
    return remember(intervalTime) {
        {
            val currentTime = System.currentTimeMillis()
            // 6. 마지막 클릭 후 intervalTime이 지났는지 확인
            // AtomicLong 써서 스레드 안전성까지 챙김
            if (currentTime - lastClickTime.load() > intervalTime) {
                lastClickTime.store(currentTime)// 7. 마지막 클릭 시간을 현재 시간으로 갱신
                currentOnClick()// 8. 실제 onClick 로직 실행
            }
        }
    }
}

/*
사용 예시
* Button(
    onClick = rememberSeSACThrottledOnClick {
        // 이 코드는 0.3초에 한 번만 실행됩니다.
        viewModel.addProduct()
    }
) {    Text("상품 추가")
}

*
* */