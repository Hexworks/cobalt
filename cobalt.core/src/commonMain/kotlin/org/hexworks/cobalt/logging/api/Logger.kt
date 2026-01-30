package org.hexworks.cobalt.logging.api

/**
 * The [Logger] interface contains all possible logging operations.
 */
interface Logger {

    /**
     * The name of this logger instance
     */
    val name: String

    /**
     * Lazily log an exception (throwable) at the TRACE level with an
     * accompanying message.
     */
    fun trace(t: Throwable? = null, msgFn: () -> String)

    /**
     * Lazily log an exception (throwable) at the DEBUG level with an
     * accompanying message.
     */
    fun debug(t: Throwable? = null, msgFn: () -> String)

    /**
     * Lazily log an exception (throwable) at the INFO level with an
     * accompanying message.
     */
    fun info(t: Throwable? = null, msgFn: () -> String)

    /**
     * Lazily log an exception (throwable) at the WARN level with an
     * accompanying message.
     */
    fun warn(t: Throwable? = null, msgFn: () -> String)

    /**
     * Lazily log an exception (throwable) at the ERROR level with an
     * accompanying message.
     */
    fun error(t: Throwable? = null, msgFn: () -> String)

    companion object
}
