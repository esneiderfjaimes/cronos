package com.nei.cronos

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.IconImageProvider
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

private val actionKey = ActionParameters.Key<String>(
    "actionKey"
)

private val favoritePreferencesKey = booleanPreferencesKey("favorite")

class SimpleWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ContentView()
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    private fun ContentView() {

        val favorite = currentState(key = favoritePreferencesKey) ?: false

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(day = Color.Black, night = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "favorite: $favorite",
                modifier = GlanceModifier
                    .clickable(
                        onClick = actionRunCallback<RefreshWidgetActionCallback>(
                            actionParametersOf(actionKey to "FAVORITE_ACTION")
                        )
                    ),
            )

        }
    }

}

class RefreshWidgetActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val action: String = parameters[actionKey] ?: return

        Log.d("MyLog", "actionKey's value is $action")

        if (action != "FAVORITE_ACTION") {
            return
        }
        updateAppWidgetState(context, glanceId) { mutablePreferences ->
            var favorite = mutablePreferences[favoritePreferencesKey]

            if (favorite != null) {
                favorite = !favorite
                mutablePreferences[favoritePreferencesKey] = favorite
            } else {
                mutablePreferences[favoritePreferencesKey] = true
            }
        }
        SimpleWidget().update(context, glanceId)
    }
}