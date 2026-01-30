package org.hexworks.cobalt.core.internal

import org.hexworks.cobalt.core.api.UUID
import kotlin.random.Random


@Suppress("EXPERIMENTAL_API_USAGE")
@OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
class DefaultUUID(val data: UByteArray) : UUID {
    override fun equals(
        other: Any?
    ): Boolean = other is DefaultUUID && this.data.contentEquals(other.data)

    override fun hashCode(): Int = this.data.contentHashCode()

    companion object {
        private const val HEX = "0123456789ABCDEF"

        private val regex =
            Regex(
                "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}",
                RegexOption.IGNORE_CASE
            )

        val NIL: UUID get() = DefaultUUID("00000000-0000-0000-0000-000000000000")

        private fun fix(data: UByteArray, version: Int, variant: Int): UByteArray {
            data[6] = ((data[6].toInt() and 0b0000_1111) or (version shl 4)).toUByte()
            data[8] = ((data[8].toInt() and 0x00_111111) or (variant shl 6)).toUByte()
            return data
        }

        fun randomDefaultUUID(random: Random = Random): UUID = DefaultUUID(fix(UByteArray(16).apply {
            random.nextBytes(this.asByteArray())
        }, version = 4, variant = 1))

        operator fun invoke(str: String): UUID {
            if (regex.matchEntire(str) == null) throw IllegalArgumentException("Invalid DefaultUUID")
            return DefaultUUID(str.replace("-", "").hexToUByteArray())
        }
    }

    val version: Int get() = (data[6].toInt() ushr 4) and 0b1111
    val variant: Int get() = (data[8].toInt() ushr 6) and 0b11

    override fun toString(): String = buildString(36) {
        for (n in 0 until 16) {
            val c = data[n].toInt()
            append(HEX[c shr 4])
            append(HEX[c and 0xF])
            if (n == 3 || n == 5 || n == 7 || n == 9) append('-')
        }
    }
}