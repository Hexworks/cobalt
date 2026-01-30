package org.hexworks.cobalt.events.internal

import org.hexworks.cobalt.core.api.behavior.DisposeState
import org.hexworks.cobalt.core.api.behavior.DisposedByException
import org.hexworks.cobalt.core.api.behavior.NotDisposed
import org.hexworks.cobalt.events.api.*
import org.hexworks.cobalt.logging.api.LoggerFactory

internal class DefaultEventBus : EventBus {

    private var closed = false

    private var subscriptions = mutableMapOf<SubscriberKey, EventSubscriptions<*>>()
    private val logger = LoggerFactory.getLogger(this::class)

    override fun <E : Event> fetchSubscribersOf(
        eventScope: EventScope,
        descriptor: EventDescriptor<E>
    ): Iterable<Subscription> {

        return subscriptions[SubscriberKey(eventScope, descriptor.key)]?.subscriptions ?: emptyList()
    }

    override fun <E : Event> subscribeTo(
        eventScope: EventScope,
        descriptor: EventDescriptor<E>,
        fn: (E) -> CallbackResult
    ): Subscription = whenNotClosed {
        try {
            logger.debug { "Subscribing to ${descriptor.key} with scope $eventScope." }
            val subscription = EventBusSubscription(
                eventScope = eventScope,
                descriptor = descriptor,
                callback = fn
            )
            val subKey = SubscriberKey(eventScope, descriptor.key)
            if (subscriptions.containsKey(subKey)) {
                val subs = subscriptions[subKey] as EventSubscriptions<E>
                subs.subscriptions.add(subscription)
            } else {
                subscriptions[subKey] = EventSubscriptions(descriptor, mutableListOf(subscription))
            }
            subscription
        } catch (e: Exception) {
            logger.warn(e) { "Failed to subscribe to event key $descriptor with scope $eventScope" }
            throw e
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun publish(
        event: Event,
        eventScope: EventScope
    ): Unit = whenNotClosed {
        logger.debug {
            "Publishing event with key ${event.key} and scope $eventScope."
        }
        subscriptions[SubscriberKey(eventScope, event.key)]?.let { subscribers ->
            subscribers.subscriptions.forEach { subscription: EventBusSubscription<*> ->
                try {
                    if (subscription.callback.fixType().invoke(event) is DisposeSubscription) {
                        subscription.dispose()
                    }
                } catch (e: Exception) {
                    logger.warn(e) { "Cancelling failed subscription $subscription." }
                    try {
                        subscription.dispose(DisposedByException(e))
                    } catch (e: Exception) {
                        logger.warn(e) { "Failed to cancel subscription $subscription." }
                    }
                }
            }
        }
    }

    override fun cancelScope(scope: EventScope): Unit = whenNotClosed {
        logger.debug { "Cancelling scope $scope." }
        subscriptions.filter { it.key.scope == scope }
            .flatMap { it.value.subscriptions }
            .forEach {
                try {
                    it.dispose()
                } catch (e: Exception) {
                    logger.warn { "Cancelling subscription failed while cancelling scope. Reason: ${e.message}" }
                }
            }
    }

    override fun close() {
        closed = true
        subscriptions.values.flatMap { it.subscriptions }.forEach { it.dispose() }
    }

    private fun <T> whenNotClosed(fn: () -> T): T {
        return if (closed) error("This Event Bus is already closed.") else fn()
    }

    private data class SubscriberKey(
        val scope: EventScope,
        val key: String
    )

    private data class EventSubscriptions<E : Event>(
        val descriptor: EventDescriptor<E>,
        val subscriptions: MutableList<EventBusSubscription<E>> = mutableListOf()
    )

    private inner class EventBusSubscription<E : Event>(
        val eventScope: EventScope,
        val descriptor: EventDescriptor<E>,
        val callback: (E) -> CallbackResult
    ) : Subscription {

        override var disposeState: DisposeState = NotDisposed
            private set

        @Suppress("UNCHECKED_CAST")
        override fun dispose(disposeState: DisposeState) {
            return try {
                logger.debug {
                    "Cancelling event bus subscription with scope '$eventScope' and key '${descriptor.key}'."
                }
                val key = SubscriberKey(eventScope, descriptor.key)
                this.disposeState = disposeState
                subscriptions[key]?.let { subs ->
                    subs.subscriptions.remove(this)
                    if (subs.subscriptions.isEmpty()) {
                        subscriptions.remove(key)
                    }
                }
                Unit
            } catch (e: Exception) {
                logger.warn(e) { "Cancelling event bus subscription failed." }
                throw e
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun ((Nothing) -> CallbackResult).fixType() = this as (Event) -> CallbackResult
}
