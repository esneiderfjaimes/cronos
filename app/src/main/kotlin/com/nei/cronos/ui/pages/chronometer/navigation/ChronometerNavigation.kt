package com.nei.cronos.ui.pages.chronometer.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nei.cronos.ui.pages.chronometer.ChronometerRoute
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8

private val URL_CHARACTER_ENCODING = UTF_8.name()

@VisibleForTesting
internal const val chronometerIdArg = "chronometerId"

internal class ChronometerArgs(val chronometerId: Int) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(savedStateHandle[chronometerIdArg]),
            URL_CHARACTER_ENCODING
        ).toInt()
    )
}

fun NavController.navigateToChronometer(topicId: Int) {
    val encodedId = URLEncoder.encode(topicId.toString(), URL_CHARACTER_ENCODING)
    this.navigate("chronometer_route/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.chronometerScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "chronometer_route/{$chronometerIdArg}",
        arguments = listOf(
            navArgument(chronometerIdArg) { type = NavType.StringType },
        ),
    ) {
        ChronometerRoute(onBackClick = onBackClick)
    }
}
