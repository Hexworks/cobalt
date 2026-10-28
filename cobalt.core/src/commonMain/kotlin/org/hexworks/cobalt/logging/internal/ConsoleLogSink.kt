package org.hexworks.cobalt.logging.internal

import org.hexworks.cobalt.logging.api.LogLevel
import org.hexworks.cobalt.logging.api.LogSink

/** Prints `[LEVEL] name: message` to standard output, followed by the stack trace if any. */
internal object ConsoleLogSink : LogSink {
    override fun log(level: LogLevel, loggerName: String, message: String, throwable: Throwable?) {
        println("[$level] $loggerName: $message")
        throwable?.printStackTrace()
    }
}
