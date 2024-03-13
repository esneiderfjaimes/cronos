@file:OptIn(ExperimentalFoundationApi::class)

package com.nei.cronos.core.designsystem.component.drawer

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nei.cronos.R
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.utils.transparentBackground
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

@Composable
fun NeiDrawer(
    state: DrawerState = rememberDrawerState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    contentDrawer: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val shadowColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val drawerWidth = with(LocalDensity.current) { DrawerWidth.toPx() }
    val offsetIsZero by remember { derivedStateOf { state.requireOffset() == 0f } }

    val configuration = LocalConfiguration.current
    val isLandscape = remember(configuration) {
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    BoxWithConstraints(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .anchoredDraggable(state = state, orientation = Orientation.Horizontal)
    ) {
        val panelOffsetX = with(LocalDensity.current) {
            if (isLandscape) DrawerWidth.toPx()
            else (maxWidth - PanelWidthHide).toPx()
        }
        state.updateAnchors(DraggableAnchors {
            DrawerValue.Show at panelOffsetX
            DrawerValue.Hide at 0f
        })

        if (!offsetIsZero) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(DrawerWidth),
                verticalArrangement = Arrangement.Center,
                content = contentDrawer
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                Text(
                    text = stringResource(R.string.made_by_nei),
                    modifier = Modifier
                        .then(
                            if (isLandscape) Modifier
                                .width(DrawerWidth)
                                .align(Alignment.BottomStart)
                            else Modifier.align(Alignment.BottomCenter)
                        )
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            ShadowPanel(
                shadowColor = shadowColor,
                alphaBackground = 0.25f
            ) {
                drawAnimation(
                    offset = state.requireOffset(),
                    isLandscape = isLandscape,
                    panelOffsetX = panelOffsetX,
                    drawerWidth = drawerWidth,
                    percentageTranslationShadow = 1f,
                    scaleEnd = 0.7f,
                )
            }

            ShadowPanel(
                shadowColor = shadowColor,
                alphaBackground = 0.5f
            ) {
                drawAnimation(
                    offset = state.requireOffset(),
                    isLandscape = isLandscape,
                    translationXExtra = 16.dp.toPx(),
                    panelOffsetX = panelOffsetX,
                    drawerWidth = drawerWidth,
                    percentageTranslationShadow = 0.5f,
                    scaleEnd = 0.75f
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val corners = if (offsetIsZero) 0.dp else DefaultDrawerShape
                    drawAnimation(
                        offset = state.requireOffset(),
                        isLandscape = isLandscape,
                        translationXExtra = 32.dp.toPx(),
                        panelOffsetX = panelOffsetX,
                        drawerWidth = drawerWidth,
                        scaleEnd = 0.8f
                    )
                    shadowEffect(
                        corners = corners,
                        shadowColor = shadowColor
                    )
                }
        ) {
            content.invoke(this)
            if (!offsetIsZero) {
                Spacer(
                    modifier = Modifier
                        // .background(Color.Green.copy(0.5f))
                        .fillMaxSize()
                        .anchoredDraggable(
                            state = state,
                            orientation = Orientation.Horizontal
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                scope.launch {
                                    state.animateTo(DrawerValue.Hide)
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun ShadowPanel(
    alphaBackground: Float,
    shadowColor: Color,
    block: GraphicsLayerScope.() -> Unit
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(block)
            .transparentBackground(
                color = MaterialTheme.colorScheme.background.copy(alphaBackground),
                elevation = DefaultDrawerShape,
                shape = RoundedCornerShape(DefaultDrawerShape),
                spotColor = shadowColor
            )
    )
}

@ThemePreviews
@Composable
fun NeiDrawerPreview() {
    val rememberDrawerState = rememberDrawerState(DrawerValue.Show)
    CronosTheme {
        CronosBackground {
            NeiDrawer(
                state = rememberDrawerState,
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
                state = rememberDrawerState,
                contentDrawer = { CronosDrawerContent(rememberDrawerState) }
            ) {
                Text(text = "Content")
            }
        }
    }
}