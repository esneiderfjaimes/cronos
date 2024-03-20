package com.nei.cronos.ui.widget

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CounterWidget : GlanceAppWidget() {
    companion object {
        val countKey = intPreferencesKey("count")
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
         Content()
         //   Text(text = "PUTAS")
        }
    }

    @Composable
    fun Content() {

        GlanceTheme {
            val count = currentState(key = countKey) ?: 0

                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                ) {
                    // create your AppWidget here
                    Text("Hello World, count = $count")
                    Button(
                        text = "Click",
                        onClick = actionRunCallback(IncrementCounterWidget::class.java)
                    )
                }
        }
    }

    override val stateDefinition: GlanceStateDefinition<*>?
        get() = super.stateDefinition
}

class SimpleCounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterWidget()
}

class  IncrementCounterWidget : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.i("Cronos", "onAction: $glanceId")
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "onAction $glanceId", Toast.LENGTH_SHORT).show()
        }
        updateAppWidgetState(context, glanceId) { pref ->
            val currentCount = pref[CounterWidget.countKey] ?: 0
            pref[CounterWidget.countKey] = currentCount + 1
        }
        CounterWidget().update(context, glanceId)
    }

}