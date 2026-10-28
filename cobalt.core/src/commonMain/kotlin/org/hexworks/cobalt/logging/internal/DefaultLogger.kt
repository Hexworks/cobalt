package org.hexworks.cobalt.logging.internal

import org.hexworks.cobalt.logging.api.LogLevel
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

internal class DefaultLogger(override val name: String) : Logger {

    override fun trace(t: Throwable?, msgFn: () -> String) = log(LogLevel.TRACE, t, msgFn)

    override fun debug(t: Throwable?, msgFn: () -> String) = log(LogLevel.DEBUG, t, msgFn)

    override fun info(t: Throwable?, msgFn: () -> String) = log(LogLevel.INFO, t, msgFn)

    override fun warn(t: Throwable?, msgFn: () -> String) = log(LogLevel.WARN, t, msgFn)

    override fun error(t: Throwable?, msgFn: () -> String) = log(LogLevel.ERROR, t, msgFn)

    private fun log(level: LogLevel, t: Throwable?, msgFn: () -> String) {
        if (level >= LoggerFactory.level && LoggerFactory.level != LogLevel.OFF) {
            LoggerFactory.sink.log(level, name, msgFn(), t)
        }
    }
}
