package com.hye.healthpossible.ui.component


import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hye.shared.navigation.ContentNavRouteDef
import com.hye.shared.theme.AppTheme
import com.hye.shared.ui.common.selectionIconMenuColor
import com.hye.shared.ui.icon.Render


class MenuBarShape(
    private val curveWidth: Dp,
    private val curveHeight: Dp
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {

        return Outline.Generic(Path().apply {
            val widthPx = with(density) { curveWidth.toPx() }
            val heightPx = with(density) { curveHeight.toPx() }
            val midX = size.width / 2

            moveTo(0f, 0f)
            lineTo(midX - widthPx, 0f)

            cubicTo(
                midX - widthPx * 0.5f, 0f,
                midX - widthPx * 0.57f, heightPx,
                midX, heightPx
            )
            cubicTo(
                midX + widthPx * 0.57f, heightPx,
                midX + widthPx * 0.5f, 0f,
                midX + widthPx, 0f
            )

            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        })
    }
}
@Composable
fun BottomBar(
    navController: NavController,
    bottomNavItems: List<BottomAppBarItem>,
    currentDestination: NavDestination?,
    showAddMissionMenu: Boolean,
    onShowAddMissionMenuChange: (Boolean) -> Unit,
) {
    val isMissionScreen =
        currentDestination?.hasRoute(ContentNavRouteDef.MissionTab::class) == true

    val fabSpec = remember(isMissionScreen) {
        bottomNavItems.resolveMissionFabSpec(isMissionScreen)
    }

    val sideTabItems = remember(bottomNavItems) {
        bottomNavItems.filter {
            it.destination != ContentNavRouteDef.MissionTab &&
                    it.destination != ContentNavRouteDef.MissionCreationTab
        }
    }

    val fabSize = AppTheme.dimens.bottomBarFabSize
    val fabOffset = AppTheme.dimens.bottomBarFabOffset
    val bottomPadding = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val curveWidth = AppTheme.dimens.bottomBarCurveWidth
    val curveHeight = AppTheme.dimens.bottomBarCurveHeight
    val contentHeight = AppTheme.dimens.bottomBarContentHeight

    val barShape = remember { MenuBarShape(curveWidth = curveWidth, curveHeight = curveHeight) }

    // 시스템 바 높이 계산
    val totalBarHeight = contentHeight + bottomPadding
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.bottomBaRleisurelyHeight + bottomPadding)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(totalBarHeight)
                .shadow(elevation = AppTheme.dimens.xxs, shape = barShape)
                .background(AppTheme.colors.background, barShape)
                .padding(bottom = bottomPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val half = sideTabItems.size / 2 + sideTabItems.size % 2

            sideTabItems.take(half).forEach {
                BottomBarItemView(
                    item = it,
                    currentDestination = currentDestination,
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            sideTabItems.drop(half).forEach {
                BottomBarItemView(
                    item = it,
                    currentDestination = currentDestination,
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }
        }
       FloatingActionButton(
            onClick = {
                if (isMissionScreen) {
                    onShowAddMissionMenuChange(!showAddMissionMenu)
                } else {
                    navController.navigate(fabSpec.destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = fabOffset)
                .size(fabSize),
            containerColor = AppTheme.colors.primary,
            shape = CircleShape
        ) {
            fabSpec.icon.Render(
                tint = AppTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun BottomBarItemView(
    item: BottomAppBarItem,
    currentDestination: NavDestination?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val isSelected =
        currentDestination?.hasRoute(item.destination::class) == true

    val interactionSource = remember { MutableInteractionSource() }
    val color = isSelected.selectionIconMenuColor

    Box(
        modifier = modifier
            .fillMaxHeight()
            .selectable(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                role = Role.Tab,
                indication = null,
                interactionSource = interactionSource
            ),
        contentAlignment = Alignment.Center
    ) {
        item.icon.Render(
            tint = color,
            modifier = Modifier.size(AppTheme.dimens.xxxxxl)
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(backgroundColor = 0xFFBDBDBD, showBackground = true, showSystemUi = true, device = "id:pixel_5")
@Composable
fun BottomAppBar_Preview(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                bottomNavItems = BottomAppBarItem.fetchBottomAppBarItems(),
                currentDestination = currentDestination,
                showAddMissionMenu = false,
                onShowAddMissionMenuChange = {},
            )

        },
        modifier = Modifier,
        topBar = { },
        snackbarHost = { },
        containerColor = Color.Gray,
        contentColor = Color.Gray,
    ){
    }
}

@Composable
fun AddMissionSpreadMenu(
    isVisible: Boolean,
    onMenu1Click: () -> Unit, // 왼쪽 (템플릿)
    onMenu2Click: () -> Unit,  // 오른쪽 (직접 추가),
    modifier: Modifier = Modifier
) {
    // 애니메이션 관리자
    val transition = updateTransition(targetState = isVisible, label = "MenuTransition")

    // 1. 퍼지는 거리 설정 (취향에 맞게 조절하세요)
    val distanceX = 80.dp  // 좌우 거리
    val distanceY = 65.dp  // 상하 거리 (위로 올라감)

    // 애니메이션 설정 (통통 튀는 효과)
    val animSpec = spring<Dp>(
        dampingRatio = 0.7f,
        stiffness = Spring.StiffnessLow
    )

    // 2. 값 변화 계산
    // X축 이동 (0 -> 70)
    val moveX by transition.animateDp(transitionSpec = { animSpec }, label = "MoveX") { state ->
        if (state) distanceX else 0.dp
    }
    // Y축 이동 (0 -> -60, 위로 올라가야 하므로 음수)
    val moveY by transition.animateDp(transitionSpec = { animSpec }, label = "MoveY") { state ->
        if (state) -distanceY else 0.dp
    }
    // 투명도 & 크기
    val alpha by transition.animateFloat(label = "Alpha") { state -> if (state) 1f else 0f }
    val scale by transition.animateFloat(label = "Scale") { state -> if (state) 1f else 0.5f }

    // 메뉴가 보일 때만 렌더링
    if (isVisible || transition.currentState) {
        Box(
            contentAlignment = Alignment.BottomCenter, // 중앙 하단 기준
            modifier = modifier.padding(bottom = 15.dp) // 메인 버튼과의 기본 간격 미세 조정
        ) {

            // [왼쪽 버튼] : X는 왼쪽(-), Y는 위쪽(-)
            SpreadMenuItem(
                text = "추천 받기", // 아이콘 확인 필요
                offsetX = -moveX,
                offsetY = moveY,
                alpha = alpha,
                scale = scale,
                onClick = onMenu1Click
            )

            // [오른쪽 버튼] : X는 오른쪽(+), Y는 위쪽(-)
            SpreadMenuItem(
                text = "직접 추가", // 아이콘 확인 필요
                offsetX = moveX,
                offsetY = moveY,
                alpha = alpha,
                scale = scale,
                onClick = onMenu2Click
            )
        }
    }
}

// 작은 버튼 그리는 함수
@Composable
private fun SpreadMenuItem(
    text: String,
    offsetX: Dp,
    offsetY: Dp,
    alpha: Float,
    scale: Float,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .scale(scale)
            .alpha(alpha)
            .clickable(onClick = onClick)
    ) {
        RoundedMenuButton(text, onClick = onClick)
    }
}
@Composable
private fun RoundedMenuButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .shadow(8.dp, CircleShape)
            .background(AppTheme.colors.primary, CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = AppTheme.colors.onPrimary,
        )
    }
}