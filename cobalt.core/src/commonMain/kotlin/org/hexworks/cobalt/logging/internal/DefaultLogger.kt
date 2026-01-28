package org.hexworks.cobalt.logging.internal

import io.github.oshai.kotlinlogging.KotlinLogging
import org.hexworks.cobalt.logging.api.Logger

internal class DefaultLogger(override val name: String) : Logger {

    private val logger = KotlinLogging.logger(name)

    override fun trace(t: Throwable?, msgFn: () -> String) {
        logger.trace(t, msgFn)
    }

    override fun debug(t: Throwable?, msgFn: () -> String) {
        logger.debug(t, msgFn)
    }

    override fun info(t: Throwable?, msgFn: () -> String) {
        logger.info(t, msgFn)
    }

    override fun warn(t: Throwable?, msgFn: () -> String) {
        logger.warn(t, msgFn)
    }

    override fun error(t: Throwable?, msgFn: () -> String) {
        logger.error(t, msgFn)
    }
}
