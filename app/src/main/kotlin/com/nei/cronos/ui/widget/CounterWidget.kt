package com.nei.cronos.ui.widget

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.nei.cronos.core.common.genTimeRange
import com.nei.cronos.feature.widget.AppWidgetColumn
import com.nei.cronos.utils.differenceParse
import cronos.core.model.ChronometerFormat
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

interface StatisticsRepository {
    fun getStatistics(): Long
    fun setStatistics(id: Long)
}

class CounterWidget : GlanceAppWidget() {
    companion object {
        val thinMode = DpSize(120.dp, 120.dp)
        val smallMode = DpSize(184.dp, 184.dp)
        val mediumMode = DpSize(260.dp, 200.dp)
        val largeMode = DpSize(260.dp, 280.dp)
    }

    // Override the state definition to use our custom one using Kotlin serialization
    override val stateDefinition = WeatherInfoStateDefinition

    // Define the supported sizes for this widget.
    // The system will decide which one fits better based on the available space
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(thinMode, smallMode, mediumMode, largeMode)
    )

    // a way to get hilt inject what you need in non-suported class
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface StatisticsProviderEntryPoint {
        fun statisticsRepository(): StatisticsRepository
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                "provideGlance $id",
                Toast.LENGTH_SHORT
            ).show()
        }
        provideContent {
            Content2()
        }
    }
}

@Composable
fun Content2() {
    // Get the stored stated based on our custom state definition.
    val widgetState = currentState<WidgetState>()
    // It will be one of the provided ones
    val size = LocalSize.current
    GlanceTheme {
        when (widgetState) {
            WidgetState.Loading -> {
                CircularProgressIndicator()
            }

            is WidgetState.Available -> {
                // Based on the size render different UI
                when (size) {
                    CounterWidget.thinMode -> WeatherThin(widgetState)
                    CounterWidget.smallMode -> WeatherSmall(widgetState)
                    CounterWidget.mediumMode -> WeatherMedium(widgetState)
                    CounterWidget.largeMode -> WeatherLarge(widgetState)
                }
            }

            is WidgetState.Unavailable -> {
                AppWidgetColumn(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Data not available")
                    Button("Refresh",
                        // actionRunCallback<UpdateWeatherAction>()
                        {
                            TODO()
                        })
                }
            }
        }
    }
}

@Composable
fun WeatherThin(widgetState: WidgetState.Available) {
    AppWidgetColumn(
        modifier = GlanceModifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val default = Locale.getDefault()
        val first = genTimeRange(widgetState.data).let { (start, end) ->
            differenceParse(
                ChronometerFormat.fromFlags(widgetState.data.formatFlags),
                default,
                start,
                end
            )
        }

        var differenceParse by remember { mutableStateOf(first) }

        LaunchedEffect(first) {
            while (true) {
                delay(1000)
                val (start, end) = genTimeRange(widgetState.data)
                differenceParse = differenceParse(
                    ChronometerFormat.fromFlags(widgetState.data.formatFlags),
                    default,
                    start,
                    end
                )
            }
        }

        Text(
            text = differenceParse,
            modifier = GlanceModifier
                .background(GlanceTheme.colors.primary)
                .padding(8.dp)
                .cornerRadius(8.dp),
            style = TextStyle(
                color = GlanceTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold
            ),
        )

        Spacer(GlanceModifier.height(8.dp))

        // create your AppWidget here
        Text(
            widgetState.data.title,
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
            ),
        )
    }
}

@Composable
fun WeatherSmall(widgetState: WidgetState.Available) {
    AppWidgetColumn(
        modifier = GlanceModifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // create your AppWidget here
        Text("Count = $widgetState")
        Text(text = "WeatherSmall")
    }
}

@Composable
fun WeatherMedium(widgetState: WidgetState.Available) {
    AppWidgetColumn(
        modifier = GlanceModifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // create your AppWidget here
        Text("Count = $widgetState")
        Text(text = "WeatherMedium")
    }
}

@Composable
fun WeatherLarge(widgetState: WidgetState.Available) {
    AppWidgetColumn(
        modifier = GlanceModifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // create your AppWidget here
        Text("Count = $widgetState")
        Text(text = "WeatherLarge")
    }
}


class SimpleCounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterWidget()
}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class BlaBla {

    @Binds
    abstract fun bindStatisticsRepository(impl: StatisticsRepositoryImpl): StatisticsRepository
}

class StatisticsRepositoryImpl @Inject constructor() : StatisticsRepository {
    override fun getStatistics(): Long = 5
    override fun setStatistics(id: Long) {}
}