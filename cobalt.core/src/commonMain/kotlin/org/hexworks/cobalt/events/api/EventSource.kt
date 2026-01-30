package org.hexworks.cobalt.events.api

import org.hexworks.cobalt.core.api.UUID

/**
 * Represents an object that can emit [Event]s and it can be
 * uniquely identified by an [id].
 */
interface EventSource {
    val id: UUID

    companion object
}