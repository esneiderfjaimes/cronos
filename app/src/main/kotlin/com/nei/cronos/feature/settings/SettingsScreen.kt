@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package com.nei.cronos.feature.settings

import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nei.cronos.BuildConfig
import com.nei.cronos.R
import com.nei.cronos.core.designsystem.component.NeiLoading
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.theme.dynamicColorScheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.utils.Mocks
import cronos.core.model.DarkThemeConfig

@Composable
internal fun SettingsRoute(
    onBackClick: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.settingsUiState.collectAsState()
    SettingsScreen(
        onBackClick = onBackClick,
        state = state,
        updateDarkThemeConfig = viewModel::updateDarkThemeConfig,
        updateDynamicColorPreference = viewModel::updateDynamicColorPreference
    )
}

@Composable
private fun SettingsScreen(
    onBackClick: () -> Unit,
    state: SettingsUiState,
    updateDarkThemeConfig: (DarkThemeConfig) -> Unit,
    updateDynamicColorPreference: (Boolean) -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.title_settings_screen)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back Navigation"
                    )
                }
            }
        )
    }) { paddingValues ->
        when (state) {
            SettingsUiState.Loading -> NeiLoading()
            is SettingsUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(
                            ScrollState(0)
                        ),
                ) {
                    Content(
                        darkThemeConfig = state.settings.darkThemeConfig,
                        useDynamicColor = state.settings.useDynamicColor,
                        updateDarkThemeConfig = updateDarkThemeConfig,
                        updateDynamicColorPreference = updateDynamicColorPreference
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Footer()
                }
            }
        }
    }
}

@Composable
fun ColumnScope.Content(
    darkThemeConfig: DarkThemeConfig,
    useDynamicColor: Boolean,
    updateDarkThemeConfig: (DarkThemeConfig) -> Unit,
    updateDynamicColorPreference: (Boolean) -> Unit
) {
    Text(
        text = "Appearance",
        style = MaterialTheme.typography.bodySmall,
        color = LocalContentColor.current.copy(alpha = 0.75f),
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val dynamicColors = dynamicColorScheme()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    updateDynamicColorPreference(!useDynamicColor)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                color = dynamicColors.primaryContainer,
                shape = CircleShape,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Opacity,
                        tint = dynamicColors.primary,
                        contentDescription = null
                    )
                }
            }
            Column {
                Text(
                    text = stringResource(R.string.use_dynamic_colors),
                    style = MaterialTheme.typography.titleMedium
                )
                AnimatedContent(
                    targetState = useDynamicColor,
                    label = "use dynamic color label",
                    transitionSpec = {
                        val offset = if (initialState >= targetState) 1 else -1

                        val transitionIn = slideInHorizontally(
                            initialOffsetX = { 50 * offset },
                            animationSpec = tween()
                        ) + fadeIn()

                        val transitionOut = slideOutHorizontally(
                            targetOffsetX = { -50 * offset },
                            animationSpec = tween()
                        ) + fadeOut()

                        transitionIn.togetherWith(transitionOut)
                    }
                ) {
                    Text(
                        text = stringYesOrNo(isYes = it),
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.75f)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = useDynamicColor,
                onCheckedChange = updateDynamicColorPreference
            )
        }
    }

    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            color = Color.Black,
            shape = CircleShape,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.DarkMode,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
        Column {
            Text(
                text = stringResource(R.string.dark_mode),
                style = MaterialTheme.typography.titleMedium
            )

            AnimatedContent(
                targetState = darkThemeConfig,
                label = "dark theme label",
                transitionSpec = {
                    val offset = if (initialState >= targetState) 1 else -1

                    val transitionIn = slideInVertically(
                        initialOffsetY = { 50 * offset },
                        animationSpec = tween()
                    ) + fadeIn()

                    val transitionOut = slideOutVertically(
                        targetOffsetY = { -50 * offset },
                        animationSpec = tween()
                    ) + fadeOut()

                    transitionIn.togetherWith(transitionOut)
                }
            ) {
                Text(
                    text = stringDarkThemeConfig(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.75f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { isExpanded = !isExpanded }) {
            Icon(
                imageVector = if (isExpanded) Icons.Rounded.ExpandLess
                else Icons.Rounded.ExpandMore,
                contentDescription = null
            )
        }
    }
    AnimatedVisibility(visible = isExpanded) {
        Column {
            DarkThemeConfig.entries.forEach { entry ->
                Item(
                    label = stringDarkThemeConfig(entry),
                    checked = darkThemeConfig == entry
                ) {
                    updateDarkThemeConfig.invoke(entry)
                }
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun ColumnScope.Footer() {
    Text(
        text = "version ${BuildConfig.VERSION_NAME}",
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(16.dp)
            .alpha(0.5f),
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
@ReadOnlyComposable
fun stringYesOrNo(isYes: Boolean): String {
    return if (isYes) stringResource(R.string.yes)
    else stringResource(R.string.no)
}

@Composable
@ReadOnlyComposable
fun stringDarkThemeConfig(darkThemeConfig: DarkThemeConfig): String {
    return when (darkThemeConfig) {
        DarkThemeConfig.OFF -> stringResource(id = R.string.off)
        DarkThemeConfig.ON -> stringResource(id = R.string.on)
        DarkThemeConfig.SYSTEM -> stringResource(id = R.string.system)
    }
}

@Composable
fun Item(
    label: String,
    checked: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = if (checked) Icons.Rounded.CheckCircle
            else Icons.Rounded.RadioButtonUnchecked,
            tint = if (checked) MaterialTheme.colorScheme.primary
            else LocalContentColor.current,
            contentDescription = null
        )
        Text(text = label)
    }
}

@ThemePreviews
@Composable
private fun SettingsScreenPreview() {
    val systemInDarkTheme = isSystemInDarkTheme()
    var darkThemeConfig by remember {
        mutableStateOf(if (systemInDarkTheme) DarkThemeConfig.ON else DarkThemeConfig.OFF)
    }
    var useDynamicColor by remember {
        mutableStateOf(true)
    }
    CronosTheme(
        dynamicColor = useDynamicColor, darkTheme = when (darkThemeConfig) {
            DarkThemeConfig.SYSTEM -> systemInDarkTheme
            DarkThemeConfig.OFF -> false
            DarkThemeConfig.ON -> true
        }
    ) {
        SettingsScreen(
            onBackClick = {},
            state = SettingsUiState.Success(
                Mocks.settingsUiStateSuccess.copy(
                    darkThemeConfig = darkThemeConfig,
                    useDynamicColor = useDynamicColor
                )
            ),
            updateDarkThemeConfig = {
                darkThemeConfig = it
            },
            updateDynamicColorPreference = {
                useDynamicColor = it
            }
        )
    }
}
