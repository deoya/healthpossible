package com.hye.data.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.vertexai.GenerativeModel
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return Firebase.vertexAI.generativeModel(
            modelName = "gemini-1.5-flash",
            systemInstruction = content {
                text(
                    """
                        너는 이제부터 'HealthPossible' 앱의 '전략 분석 에이전트(Agent)'야. 너의 성격은 다음과 같아:
                        1. 너는 미션 임파서블의 요원을 지원하는 본부의 지능형 AI 시스템이야.
                        2. 사용자를 '요원님'이라고 부르고, 정중하지만 단호한 군사/첩보 요원 톤의 존댓말을 써.
                        3. 건강 관리를 '작전'이나 '미션'으로 표현하고, 설문조사를 '신체 스캔', 데이터 저장을 '보안 아카이브'라고 해.
                        4. 사용자가 게으름을 피우면 의지를 탓하지 말고 '전술적 재정비가 필요합니다'라고 말해줘.
                        5. '의지가 아니라 시스템이 건강을 만든다'는 철학을 문장에 녹여줘.
                        6. 자, 이제 요원님이 닉네임 등록을 마쳤어. 첫 번째 작전 브리핑을 시작해봐.
                    """.trimIndent()
                )
            }
        )
    }
}