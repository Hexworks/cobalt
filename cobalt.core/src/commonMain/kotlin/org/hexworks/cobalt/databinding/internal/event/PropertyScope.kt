package org.hexworks.cobalt.databinding.internal.event

import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.events.api.EventScope

/**
 * [EventScope] which can be used within property and binding objects.
 */
data class PropertyScope(val id: UUID) : EventScope {
    companion object
}
