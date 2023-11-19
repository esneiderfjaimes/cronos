package com.nei.cronos.core.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews

@Composable
fun AddChronometerAction(
    formattedValue: String?,
    onClick: () -> Unit,
    onCloseClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    IconButton(
        onClick = onClick,
        active = formattedValue != null,
        trailingIcon = {
            formattedValue?.let {
                Text(text = formattedValue, maxLines = 1)
                MyIconButton(onClick = onCloseClick) {
                    Icon(Icons.Outlined.Close, contentDescription = null)
                }
            }
        }
    ) {
        content.invoke()
    }
}

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    active: Boolean = false,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    trailingIcon: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(CircleShape)
            .then(
                if (active) {
                    Modifier
                        .height(40.dp)
                        .clickable(onClick = onClick)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                } else {
                    Modifier
                        .size(40.dp)
                        .clickable(
                            onClick = onClick,
                            enabled = enabled,
                            role = Role.Button,
                            interactionSource = interactionSource,
                            indication = rememberRipple(
                                bounded = false,
                                radius = 40.dp / 2
                            )
                        )
                }
            )

            .background(color = colors.containerColor(enabled))
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        val contentColor = colors.contentColor(enabled)
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) { content.invoke() }
            trailingIcon.invoke(this)
        }
    }
}


@Composable
fun MyIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color = colors.containerColor(enabled))
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = 40.dp / 2
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentColor = colors.contentColor(enabled)
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}

fun IconButtonColors.containerColor(enabled: Boolean): Color =
    if (enabled) containerColor else disabledContainerColor

fun IconButtonColors.contentColor(enabled: Boolean): Color =
    if (enabled) contentColor else disabledContentColor

@ThemePreviews
@Composable
fun AddChronometerActionPreview() {
    var formattedValue by remember { mutableStateOf<String?>("null") }
    CronosTheme {
        CronosBackground {
            AddChronometerAction(
                formattedValue = formattedValue,
                onClick = { formattedValue = "00:00" },
                onCloseClick = { formattedValue = null }
            ) {
                Icon(Icons.Outlined.Schedule, contentDescription = null)
            }
        }
    }
}