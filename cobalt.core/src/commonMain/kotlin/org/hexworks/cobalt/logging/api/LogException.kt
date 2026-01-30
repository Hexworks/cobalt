package org.hexworks.cobalt.logging.api

class LogException(
    override val message: String,
    val other: Throwable?
) : Exception(message, other) {
    override fun toString() = "$message : $other"
}