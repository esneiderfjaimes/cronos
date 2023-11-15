@file:OptIn(ExperimentalFoundationApi::class)

package com.nei.cronos.core.designsystem.component

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp

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

const val paddingDrawer = 92

@Composable
fun NeiDrawer(
    drawerState: DrawerState = rememberDrawerState(),
    contentDrawer: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        val drawerWidth = with(LocalDensity.current) { minWidth.toPx() - paddingDrawer.dp.toPx() }
        drawerState.updateAnchors(DraggableAnchors {
            DrawerValue.Show at drawerWidth
            DrawerValue.Hide at 0f
        })
        val shapeContent = with(LocalDensity.current) { 32.dp.toPx() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(state = drawerState, orientation = Orientation.Horizontal)
        )

        Column(
            modifier = Modifier
                .padding(end = paddingDrawer.dp)
                .align(Alignment.Center)
                .fillMaxWidth(),
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

        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                onGraphicsLayer(
                    offset = drawerState.requireOffset(),
                    translationXDiv = 1.25f,
                    scale = 0.7f,
                    drawerWidth = drawerWidth,
                    corners = shapeContent
                )
            }
            .background(MaterialTheme.colorScheme.background.copy(0.5f))
            .anchoredDraggable(state = drawerState, orientation = Orientation.Horizontal)
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                onGraphicsLayer(
                    offset = drawerState.requireOffset(),
                    translationXDiv = 1.125f,
                    scale = 0.75f,
                    drawerWidth = drawerWidth,
                    corners = shapeContent
                )
            }
            .background(MaterialTheme.colorScheme.background.copy(0.75f))
            .anchoredDraggable(state = drawerState, orientation = Orientation.Horizontal)
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                val corners = if (drawerState.requireOffset() == 0f) 0f else shapeContent
                onGraphicsLayer(
                    offset = drawerState.requireOffset(),
                    scale = 0.8f,
                    drawerWidth = drawerWidth,
                    corners = corners
                )
            }
            .anchoredDraggable(state = drawerState, orientation = Orientation.Horizontal),
            content = content
        )
    }
}

private fun GraphicsLayerScope.onGraphicsLayer(
    offset: Float,
    translationXDiv: Float = 1f,
    scale: Float,
    drawerWidth: Float,
    corners: Float
) {
    translationX = offset / translationXDiv
    val scaleTransform = lerp(1f, scale, offset / drawerWidth)
    scaleX = scaleTransform
    scaleY = scaleTransform
    clip = true
    shadowElevation = corners
    shape = RoundedCornerShape(corners)
}
