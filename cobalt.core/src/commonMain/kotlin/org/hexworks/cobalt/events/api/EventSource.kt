package org.hexworks.cobalt.events.api

/**
 * Represents an object that can emit [Event]s and it can be
 * uniquely identified by an [id].
 */
interface EventSource {
    val id: String
}