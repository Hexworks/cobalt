package org.hexworks.cobalt.events.api

/**
 * Common interface for all [Event]s which can be sent using the [EventBus]. Each event
 * must have a [key] which can be used to group events from the same origin / cause together.
 * [trace] can be used to check the chain of events which caused this [Event]. Each [Event]
 * must also have an [emitter] which is the object responsible for emitting this [Event].
 */
interface Event {

    /**
     * A unique key for this [Event].
     */
    val key: String

    /**
     * The object that emitted *this* [Event].
     */
    val emitter: EventSource

    /**
     * Contains a (possibly empty) sequence of [Event]s that lead up to *this* [Event] in reverse
     * chronological order (most recent is first, oldest is last).
     * eg: `listOf(lastEvent, eventBefore, firstEvent)
     */
    val trace: Iterable<Event>
        get() = listOf()

    companion object
}
