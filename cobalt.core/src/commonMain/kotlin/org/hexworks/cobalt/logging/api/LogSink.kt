package org.hexworks.cobalt.logging.api

/**
 * Receives every log statement that passes the [LoggerFactory.level] threshold.
 * Install your own with [LoggerFactory.sink] to route logs into any logging framework.
 */
fun interface LogSink {
    fun log(level: LogLevel, loggerName: String, message: String, throwable: Throwable?)
}
