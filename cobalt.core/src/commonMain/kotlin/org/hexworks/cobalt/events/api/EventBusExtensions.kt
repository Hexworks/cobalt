package org.hexworks.cobalt.events.api

import org.hexworks.cobalt.events.internal.ApplicationScope

/**
 * Same as [EventBus.subscribeTo], but will always [KeepSubscription] when
 * the callback is called.
 */
fun <E : Event> EventBus.simpleSubscribeTo(
    descriptor: EventDescriptor<E>,
    eventScope: EventScope = ApplicationScope,
    callback: (E) -> Unit
): Subscription {
    return subscribeTo(
        eventScope = eventScope,
        descriptor = descriptor,
        fn = {
            callback(it)
            KeepSubscription
        }
    )
}
