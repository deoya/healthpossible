package com.hye.data.api.mapper

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "XML")
data class DiseaseGuidelineResponse(
    @Element(name = "HEAD")
    val head: GuidelineHead?,

    @Element(name = "svc")
    val svc: GuidelineService?
)

@Xml(name = "HEAD")
data class GuidelineHead(
    @PropertyElement(name = "CODE")
    val code: String?,

    @PropertyElement(name = "MESSAGE")
    val message: String?
)

@Xml(name = "svc")
data class GuidelineService(
    @PropertyElement(name = "CNTNTSSJ")
    val title: String?, // 예: 당뇨환자의 운동요법

    @Element(name = "cntntsClList")
    val contentList: ContentList?
)

@Xml(name = "cntntsClList")
data class ContentList(
    @Element(name = "cntntsCl")
    val contents: List<ContentItem>?
)

@Xml(name = "cntntsCl")
data class ContentItem(
    @PropertyElement(name = "CNTNTS_CL_NM")
    val categoryName: String?, // 예: 개요, 평가 및 검사, 실천 방법

    @PropertyElement(name = "CNTNTS_CL_CN")
    val htmlContent: String?   // CDATA로 묶인 HTML 텍스트
)