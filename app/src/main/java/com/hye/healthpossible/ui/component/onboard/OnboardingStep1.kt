package com.hye.healthpossible.ui.component.onboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.hye.domain.validation.CodeNamePolicy
import com.hye.healthpossible.R
import com.hye.shared.theme.AppTheme
import com.hye.shared.theme.toSp
import com.hye.shared.ui.text.ErrorMessage
import com.hye.shared.ui.text.LabelSmall
import com.hye.shared.ui.text.StyledInputField
import com.hye.shared.ui.text.TextSubheading
import com.hye.shared.ui.text.TrailingIcon
import com.hye.shared.util.text

@Composable
fun OnboardingStep1(
    codename: String,
    isChecking: Boolean,
    isValid: Boolean,
    errorMessage: String,
    onCodenameChange: (String) -> Unit,
    onDebouncedCodenameChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    OnboardingStep1Content(
        codename = codename,
        isChecking = isChecking,
        isValid = isValid,
        errorMessage = errorMessage,
        onCodenameChange = { if (it.length <= CodeNamePolicy.CODENAME_MAX_LEN) onCodenameChange(it) },
        onDebouncedCodenameChange = onDebouncedCodenameChange,
        onKakaoLoginClick = { /* Todo : 카카오 로그인 로직 연동 */ },
        onNextClick = onNext
    )
}

@Composable
fun OnboardingStep1Content(
    codename: String,
    isChecking: Boolean,
    isValid: Boolean,
    errorMessage: String,
    onCodenameChange: (String) -> Unit,
    onDebouncedCodenameChange: (String) -> Unit,
    onKakaoLoginClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxWidth()
                .background(AppTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = R.drawable.profile_onboarding,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .weight(0.55f)
                .padding(AppTheme.dimens.xxl),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = R.string.first_greeting.text,
                    color = AppTheme.colors.mainColor,
                    style = MaterialTheme.typography.headlineSmall,
                )
                TextSubheading(
                    text = R.string.second_greeting.text,
                    lineHeight = AppTheme.dimens.xxl.toSp,
                    modifier = Modifier.padding(top = AppTheme.dimens.xxs, bottom = AppTheme.dimens.xxxxl)
                )
                // Todo : 중복일시 카카오계정에 백업할 때 수정을 해야할 수도 있다고 명시하는 방향으로 전향하기
                StyledInputField.Section(
                    label = { LabelSmall(R.string.codename.text) },
                    value = codename,
                    onValueChange = onCodenameChange,
                    debounceInterval = 500L,
                    onDebouncedValueChange = onDebouncedCodenameChange,
                    isError = errorMessage.isNotEmpty(),
                    trailingIcon = {
                        TrailingIcon(
                            isChecking = isChecking,
                            isValid = isValid,
                            errorMessage = errorMessage
                        )
                    },
                    keyboardActions = { focusManager.clearFocus() },
                    description = {
                        if (errorMessage.isNotEmpty()) {
                            ErrorMessage(errorMessage)
                        }
                    }
                )

                Text(
                    text = R.string.onboarding_link_kakao_description.text,
                    modifier = Modifier.padding(top = AppTheme.dimens.m)
                        .clickable { onKakaoLoginClick() },
                    color = AppTheme.colors.textSecondary,
                    fontSize = AppTheme.dimens.sm.toSp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.s)
            ) {

                Button(
                    onClick = onNextClick,
                    enabled = isValid && !isChecking,
                    modifier = Modifier
                        .weight(1f)
                        .height(AppTheme.dimens.bigBtn),
                    shape = RoundedCornerShape(AppTheme.dimens.md),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.mainColor,
                        disabledContainerColor = AppTheme.colors.inactive
                    ),
                    elevation = ButtonDefaults.buttonElevation(if (isValid) AppTheme.dimens.xxxxs else 0.dp)
                ) {
                    Text(R.string.start_button.text, fontSize = AppTheme.dimens.md.toSp, fontWeight = FontWeight.Bold)
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(start = AppTheme.dimens.xxs)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "1. 초기 기본 상태")
@Composable
fun OnboardingStep1Preview_Default() {
    OnboardingStep1Content(
        codename = "",
        isChecking = false,
        isValid = false,
        errorMessage = "",
        onCodenameChange = {},
        onDebouncedCodenameChange = {},
        onKakaoLoginClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true, name = "2. 에러 상태 (사용 불가 닉네임)")
@Composable
fun OnboardingStep1Preview_Error() {
    OnboardingStep1Content(
        codename = "운영자",
        isChecking = false,
        isValid = false,
        errorMessage = "사용할 수 없는 단어가 포함되어 있어요.",
        onCodenameChange = {},
        onDebouncedCodenameChange = {},
        onKakaoLoginClick = {},
        onNextClick = {}
    )
}

@Preview(showBackground = true, name = "3. 통과 상태 (버튼 활성화)")
@Composable
fun OnboardingStep1Preview_Valid() {
    OnboardingStep1Content(
        codename = "스쿼트요정",
        isChecking = false,
        isValid = true,
        errorMessage = "",
        onCodenameChange = {},
        onDebouncedCodenameChange = {},
        onKakaoLoginClick = {},
        onNextClick = {}
    )
}
