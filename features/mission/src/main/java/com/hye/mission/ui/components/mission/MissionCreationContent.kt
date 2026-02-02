package com.hye.mission.ui.components.mission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hye.features.mission.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.CardStyle
import com.hye.shared.ui.StyledCard
import com.hye.shared.ui.text.TitleSmall
import com.hye.shared.util.text

@Composable
fun MissionCreationContent(
    commonInputContent: @Composable () -> Unit,
    categoryTabContent: @Composable () -> Unit,
    detailedFormContent: @Composable () -> Unit,
    tagInputContent: @Composable () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(AppTheme.dimens.md),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.xxl)
    ) {
        // 1. 공통 입력 섹션 (카드)
        StyledCard {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.l),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.l)
            ) {
                commonInputContent()
            }
        }

        // 2. 카테고리 선택 & 상세 설정
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)) {
            TitleSmall(R.string.mission_plan_category_setting.text)

            // 탭 영역
            categoryTabContent() 

            // 상세 설정 폼 영역 (카드)
            StyledCard {
                Column(modifier = Modifier.padding(AppTheme.dimens.l)) {
                    detailedFormContent() 
                }
            }
        }

        // 3. 태그 입력 섹션 (카드)
        StyledCard(
            CardStyle(modifier = Modifier.padding(bottom = AppTheme.dimens.xxxxxxl).fillMaxWidth())
        ) {
            Column(modifier = Modifier.padding(AppTheme.dimens.l)) {
                tagInputContent() 
            }
        }
    }
}
