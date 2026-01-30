package org.hexworks.cobalt.events.api

import kotlin.reflect.KClass

/**
 * Uniquely identifies an [Event] that can be sent with the given [key].
 */
interface EventDescriptor<E: Event> {
    val key: String
    val eventType: KClass<E>
}