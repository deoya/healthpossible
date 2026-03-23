package com.hye.domain.model.profile

enum class ChronicDiseaseCode(val diseaseName: String, val contentSn: String) {
    DIABETES("당뇨병", "3390"),
    HYPERTENSION("고혈압", "6765"),
    HYPERLIPIDEMIA("고지혈증", "6715"),
    ARTHRITIS("관절염", "1988"),
    THYROID("갑상선 질환", "5810"),
    ANEMIA("빈혈", "1104"),
    OBESITY("비만", "6694");

    companion object {
        // "당뇨병"이라는 String을 넣으면 "3390"을 뱉어내는 유틸 함수
        fun getSnByName(name: String): String? {
            return entries.find { it.diseaseName == name }?.contentSn
        }
    }
}