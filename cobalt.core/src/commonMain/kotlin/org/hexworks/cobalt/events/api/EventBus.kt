package org.hexworks.cobalt.events.api

import org.hexworks.cobalt.events.internal.ApplicationScope
import org.hexworks.cobalt.events.internal.DefaultEventBus

/**
 * An [EventBus] can be used to broadcast [Event]s to subscribers of that [Event].
 */
interface EventBus {

    /**
     * Returns all subscribers of the event with the given [EventDescriptor] and [eventScope].
     */
    fun <E: Event> fetchSubscribersOf(
        descriptor: EventDescriptor<E>,
        eventScope: EventScope = ApplicationScope,
    ): Iterable<Subscription>

    /**
     * Subscribes the callee to [Event]s which have [eventScope] and [EventDescriptor].
     * [fn] will be called whenever there is a matching event. [CallbackResult] can
     * be used to control the [Subscription]:
     * - [KeepSubscription] will keep the [Subscription]
     * - [DisposeSubscription] will dispose it
     */
    fun <E : Event> subscribeTo(
        descriptor: EventDescriptor<E>,
        eventScope: EventScope = ApplicationScope,
        fn: (E) -> CallbackResult
    ): Subscription

    /**
     * Publishes the given [Event] to all listeners who have the same
     * [eventScope] and [Event.key].
     */
    fun publish(
        event: Event,
        eventScope: EventScope = ApplicationScope
    )

    /**
     * Cancels all [Subscription]s for the given [scope].
     */
    fun cancelScope(scope: EventScope)

    /**
     * Cancels all subscriptions and closes this [EventBus].
     */
    fun close()

    companion object {

        /**
         * Creates a new [EventBus].
         */
        fun create(): EventBus = DefaultEventBus()
    }
}
