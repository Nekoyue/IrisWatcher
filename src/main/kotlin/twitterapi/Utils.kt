package twitterapi

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer that encodes and decodes [ULong] as its string representation.
 *
 * Intended to be used for interoperability with external clients (mainly JavaScript ones),
 * where numbers can't be parsed correctly if they exceed
 * [`abs(2^53-1)`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER).
 */
object ULongAsStringSerializer : KSerializer<ULong> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("twitterapi.ULongAsStringSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ULong) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ULong {
        return decoder.decodeString().toULong()
    }
}
