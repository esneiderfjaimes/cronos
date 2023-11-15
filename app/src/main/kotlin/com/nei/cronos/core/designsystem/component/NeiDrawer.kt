@file:OptIn(ExperimentalFoundationApi::class)

package com.nei.cronos.core.designsystem.component

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

@Composable
fun NeiDrawer(
    drawerState: DrawerState = rememberDrawerState(),
    contentDrawer: @Composable BoxWithConstraintsScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        val drawerWidth = with(LocalDensity.current) { minWidth.toPx() - 92.dp.toPx() }
        drawerState.updateAnchors(DraggableAnchors {
            DrawerValue.Show at drawerWidth
            DrawerValue.Hide at 0f
        })
        val shapeContent = with(LocalDensity.current) { 32.dp.toPx() }
        Surface(
            modifier = Modifier.padding(end = 80.dp)
        ) {
            contentDrawer()
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.translationX = drawerState.requireOffset()
                val scale = lerp(1f, 0.8f, drawerState.requireOffset() / drawerWidth)
                scaleX = scale
                scaleY = scale
                val corners =
                    if (drawerState.currentValue == DrawerValue.Show) shapeContent else 0f
                clip = true
                shadowElevation = corners
                shape = RoundedCornerShape(corners)
            }
            .anchoredDraggable(
                state = drawerState,
                orientation = Orientation.Horizontal,
            ),
            content = content
        )
    }
}
