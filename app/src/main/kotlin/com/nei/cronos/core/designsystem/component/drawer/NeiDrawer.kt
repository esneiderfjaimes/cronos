@file:OptIn(ExperimentalFoundationApi::class)

package com.nei.cronos.core.designsystem.component.drawer

import android.annotation.SuppressLint
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.ui.contents.CronosDrawerContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class DrawerValue { Show, Hide }
typealias DrawerState = AnchoredDraggableState<DrawerValue>

@Composable
fun rememberDrawerState(initialValue: DrawerValue = DrawerValue.Hide): DrawerState {
    val velocityThreshold: Float = with(LocalDensity.current) { 80.dp.toPx() }
    return remember {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { velocityThreshold },
            animationSpec = spring()
        )
    }
}

val DrawerWidth = 300.dp
val PanelWidthHide = 72.dp
val DefaultDrawerShape = 32.dp

@SuppressLint("RestrictedApi")
@Composable
fun NeiDrawer(
    drawerState: DrawerState = rememberDrawerState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentDrawer: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val shadowColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val drawerWidth = with(LocalDensity.current) { DrawerWidth.toPx() }
    val offsetIsZero by remember { derivedStateOf { drawerState.requireOffset() == 0f } }

    BoxWithConstraints(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .anchoredDraggable(state = drawerState, orientation = Orientation.Horizontal)
    ) {
        val panelOffsetX = with(LocalDensity.current) { (maxWidth - PanelWidthHide).toPx() }
        drawerState.updateAnchors(DraggableAnchors {
            DrawerValue.Show at panelOffsetX
            DrawerValue.Hide at 0f
        })

        if (!offsetIsZero) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(DrawerWidth),
                verticalArrangement = Arrangement.Center
            ) {
                contentDrawer()
            }
            Text(
                text = "Made by Nei \uD83C\uDDE8\uD83C\uDDF4",
                modifier = Modifier
                    .padding(12.dp)
                    .navigationBarsPadding()
                    .align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    drawAnimation(
                        offset = drawerState.requireOffset(),
                        scaleEnd = 0.7f,
                        percentageTranslationShadow = 1f,
                        drawerWidth = drawerWidth,
                        panelOffsetX = panelOffsetX,
                        color = shadowColor
                    )
                }
                .background(MaterialTheme.colorScheme.background.copy(0.5f))
            )

            Spacer(modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    drawAnimation(
                        offset = drawerState.requireOffset(),
                        scaleEnd = 0.75f,
                        percentageTranslationShadow = 0.5f,
                        drawerWidth = drawerWidth,
                        panelOffsetX = panelOffsetX,
                        color = shadowColor
                    )
                }
                .background(MaterialTheme.colorScheme.background.copy(0.75f))
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val offset = drawerState.requireOffset()
                    val corners = if (offset == 0f) 0.dp else DefaultDrawerShape
                    drawAnimation(
                        offset = drawerState.requireOffset(),
                        scaleEnd = 0.8f,
                        drawerWidth = drawerWidth,
                        color = shadowColor,
                        corners = corners,
                        panelOffsetX = panelOffsetX
                    )
                }
        ) {
            content.invoke(this)
            if (!offsetIsZero) {
                Spacer(
                    modifier = Modifier
                        // .background(Color.Green.copy(0.5f))
                        .fillMaxSize()
                        .neiDraggable(state = drawerState, scope = scope)
                )
            }
        }
    }
}

fun Modifier.neiDraggable(state: DrawerState, scope: CoroutineScope): Modifier {
    return anchoredDraggable(
        state = state,
        orientation = Orientation.Horizontal
    ).pointerInput(Unit) {
        detectTapGestures {
            scope.launch {
                state.animateTo(DrawerValue.Hide)
            }
        }
    }
}

@ThemePreviews
@Composable
fun NeiDrawerPreview() {
    val rememberDrawerState = rememberDrawerState(DrawerValue.Show)
    CronosTheme {
        CronosBackground {
            NeiDrawer(
                drawerState = rememberDrawerState,
                contentDrawer = { CronosDrawerContent(rememberDrawerState) }) {
                Scaffold { paddingValuers ->
                    Text(text = "Content", modifier = Modifier.padding(paddingValuers))
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun NeiDrawerHidePreview() {
    val rememberDrawerState = rememberDrawerState(DrawerValue.Hide)
    CronosTheme {
        CronosBackground {
            NeiDrawer(
                drawerState = rememberDrawerState,
                contentDrawer = { CronosDrawerContent(rememberDrawerState) }
            ) {
                Text(text = "Content")
            }
        }
    }
}