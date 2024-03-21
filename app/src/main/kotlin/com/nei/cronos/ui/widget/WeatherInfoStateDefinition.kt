package com.nei.cronos.ui.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.io.File
import java.io.InputStream
import java.io.OutputStream


/**
 * Provides our own definition of "Glance state" using Kotlin serialization.
 */
object WeatherInfoStateDefinition : GlanceStateDefinition<WidgetState> {

    private const val DATA_STORE_FILENAME = "widget-"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<WidgetState> {
        val applicationContext = context.applicationContext
        return DataStoreFactory.create(
            serializer = WeatherInfoSerializer,
            produceFile = { applicationContext.dataStoreFile(DATA_STORE_FILENAME + fileKey) },
            corruptionHandler = null,
            scope =  CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.preferencesDataStoreFile(DATA_STORE_FILENAME)
    }

    /**
     * Custom serializer for WeatherInfo using Json.
     */
    object WeatherInfoSerializer : Serializer<WidgetState> {
        override val defaultValue = WidgetState.Unavailable("no place found")

        override suspend fun readFrom(input: InputStream): WidgetState = try {
            Json.decodeFromString(
                WidgetState.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            WidgetState.Unavailable(exception.message ?: "Unknown error")
            // throw CorruptionException("Could not read weather data: ${exception.message}")
        }

        override suspend fun writeTo(t: WidgetState, output: OutputStream) {
            output.use {
                it.write(
                    Json.encodeToString(WidgetState.serializer(), t).encodeToByteArray()
                )
            }
        }
    }
}
