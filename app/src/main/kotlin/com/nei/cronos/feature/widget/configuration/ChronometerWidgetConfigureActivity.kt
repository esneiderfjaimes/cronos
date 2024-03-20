@file:OptIn(ExperimentalFoundationApi::class)

package com.nei.cronos.feature.widget.configuration

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PauseCircleOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nei.cronos.core.datastore.presentation.LocalSettingsState
import com.nei.cronos.core.designsystem.component.ChronometerChip
import com.nei.cronos.core.designsystem.component.ChronometerListItem
import com.nei.cronos.core.designsystem.component.CronosBackground
import com.nei.cronos.core.designsystem.theme.CronosTheme
import com.nei.cronos.core.designsystem.utils.ThemePreviews
import com.nei.cronos.core.designsystem.utils.getLocale
import com.nei.cronos.domain.models.SectionUi
import com.nei.cronos.saveTitlePref
import com.nei.cronos.ui.main.MainUiState
import com.nei.cronos.ui.main.MainViewModel
import com.nei.cronos.ui.main.shouldUseDarkTheme
import com.nei.cronos.ui.main.shouldUseDynamicTheming
import com.nei.cronos.ui.pages.home.HomeViewModel
import com.nei.cronos.updateAppWidget
import com.nei.cronos.utils.Mocks
import com.nei.cronos.utils.differenceParse
import cronos.core.database.models.SectionEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChronometerWidgetConfigureActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var onChronometerClick = { id: Long ->
        val context = this@ChronometerWidgetConfigureActivity

        // When the button is clicked, store the string locally
        // TODO set here id chronometer
        saveTitlePref(context, appWidgetId, "id:$id")

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        var uiState: MainUiState by mutableStateOf(MainUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState = it }.collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainUiState.Loading -> true
                is MainUiState.Success -> false
            }
        }

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            if (uiState is MainUiState.Success) {
                val success = uiState as MainUiState.Success
                CompositionLocalProvider(
                    LocalSettingsState provides success.settingsState
                ) {
                    CronosTheme(
                        darkTheme = darkTheme,
                        dynamicColor = shouldUseDynamicTheming(uiState)
                    ) {
                        HomeRoute(
                            onChronometerClick = onChronometerClick,
                            onCloseClick = ::finish
                        )
                    }
                }
            }
        }
    }
}

typealias OnChronometerClick = (Long) -> Unit

@Composable
fun HomeRoute(
    onChronometerClick: OnChronometerClick,
    onCloseClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    HomeScreen(
        state = state,
        onChronometerClick = onChronometerClick,
        onCloseClick = onCloseClick
    )
}

@Composable
private fun HomeScreen(
    state: HomeViewModel.HomeState,
    onChronometerClick: OnChronometerClick = {},
    onCloseClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Configure Widget") }, actions = {
                IconButton(onClick = onCloseClick) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close")
                }
            })
        }
    ) { paddingValues ->
        HomeContent(
            state = state,
            paddingValues = paddingValues,
            onChronometerClick = onChronometerClick
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeViewModel.HomeState,
    paddingValues: PaddingValues = PaddingValues(),
    onChronometerClick: OnChronometerClick = {},
) {
    if (state.isLoading) {
        LoadingContent(paddingValues)
        return
    }

    if (state.sections.isEmpty()) {
        EmptyChronometersContent(paddingValues)
        return
    }

    SectionsContent(
        sections = state.sections,
        paddingValues = paddingValues,
        onChronometerClick = onChronometerClick
    )
}

@Composable
private fun LoadingContent(paddingValues: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun EmptyChronometersContent(paddingValues: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) { Text(text = "No chronometers") }
}

@Composable
private fun SectionsContent(
    sections: List<SectionUi> = emptyList(),
    paddingValues: PaddingValues = PaddingValues(),
    onChronometerClick: OnChronometerClick,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = paddingValues,
    ) {
        stickyHeader {
            Text(
                text = "Select a stopwatch for the widget.",
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            )
        }

        sections.forEach { section ->
            sectionContent(
                section = section,
                onChronometerClick = onChronometerClick
            )
        }
    }
}

private fun LazyListScope.sectionContent(
    section: SectionUi,
    onChronometerClick: OnChronometerClick
) {
    if (section.id != SectionEntity.NONE_SECTION_ID) {
        item(
            key = "h${section.id}",
            contentType = section.name
        ) {
            Text(text = section.name)
        }
    }
    items(
        items = section.chronometers,
        key = { "s${section.id}c${it.id}" }
    ) { chronometer ->
        if (!chronometer.isPaused) {
            if (chronometer.lastEvent == null) {
                ChronometerListItem(
                    time = chronometer.createdAt,
                    title = chronometer.title,
                    format = chronometer.format
                ) {
                    onChronometerClick.invoke(chronometer.id)
                }
            } else {
                ChronometerListItem(
                    time = chronometer.lastEvent.time,
                    title = chronometer.title,
                    format = chronometer.format
                ) {
                    onChronometerClick.invoke(chronometer.id)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onChronometerClick.invoke(chronometer.id) })
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val locale = getLocale()
                val label by remember(chronometer.lastTimeRunning, chronometer.format, locale) {
                    mutableStateOf(
                        differenceParse(
                            chronometer.format,
                            locale,
                            chronometer.lastTimeRunning,
                            chronometer.lastEvent!!.time
                        )
                    )
                }
                Box {
                    ChronometerChip(text = label, modifier = Modifier.padding(end = 14.dp))
                    Icon(
                        Icons.Rounded.PauseCircleOutline,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = chronometer.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            HorizontalDivider()
        }
    }
}

@ThemePreviews
@Composable
fun HomeScreenPreview() {
    CronosTheme {
        CronosBackground {
            HomeScreen(
                state = HomeViewModel.HomeState(
                    sections = Mocks.previewSections
                ),
            )
        }
    }
}
