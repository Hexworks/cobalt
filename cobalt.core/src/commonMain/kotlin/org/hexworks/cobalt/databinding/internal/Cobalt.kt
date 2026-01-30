package org.hexworks.cobalt.databinding.internal

import org.hexworks.cobalt.events.api.EventBus

/**
 * This object holds state, that is used internally by Cobalt. You can use
 * this but be mindful that you can break something if you do.
 */
object Cobalt {

    /**
     * The default [org.hexworks.cobalt.events.api.EventBus] instance used by Cobalt.
     */
    val eventbus: EventBus = EventBus.Companion.create()
}