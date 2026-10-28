package org.hexworks.cobalt.logging.api

import org.hexworks.cobalt.logging.internal.ConsoleLogSink
import org.hexworks.cobalt.logging.internal.DefaultLogger
import kotlin.reflect.KClass

/**
 * Creates [Logger]s and holds the global logging configuration. Cobalt has no logging
 * framework dependency: statements at or above [level] go to [sink], which prints to the
 * console unless replaced.
 */
object LoggerFactory {

    /** Minimum level that gets logged. Defaults to [LogLevel.INFO]. */
    var level: LogLevel = LogLevel.INFO

    /** Destination for log statements. Defaults to a console printer. */
    var sink: LogSink = ConsoleLogSink

    fun getLogger(name: String): Logger = DefaultLogger(name)

    fun getLogger(kClass: KClass<out Any>): Logger =
        getLogger(kClass.simpleName ?: "name-is-missing-try-using-the-string-overload")
}
