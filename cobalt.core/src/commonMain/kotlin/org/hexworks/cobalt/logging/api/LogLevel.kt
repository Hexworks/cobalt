package org.hexworks.cobalt.logging.api

/**
 * Severity of a log statement, in ascending order. [OFF] disables logging when used as
 * the threshold in [LoggerFactory.level].
 */
enum class LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, OFF
}
