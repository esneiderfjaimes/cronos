package com.nei.cronos.ui.widget

import cronos.core.model.EventType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime

@Serializable
sealed interface WidgetState {
    @Serializable
    data object Loading : WidgetState

    @Serializable
    data class Available(
        val data: WidgetData
    ) : WidgetState

    @Serializable
    data class Unavailable(val message: String) : WidgetState
}

@Serializable
data class WidgetData(
    val chronometerId: Long,
    val title: String,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val lastEventZonedDateTime: ZonedDateTime?,
    val lastEventType: EventType?,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val lastTimeRunning: ZonedDateTime,
    val formatFlags: Int
)

object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val string = decoder.decodeString()
        return ZonedDateTime.parse(string)
    }
}