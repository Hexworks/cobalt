package org.hexworks.cobalt.databinding.internal.property

import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.databinding.internal.event.PropertyScope
import org.hexworks.cobalt.logging.api.Logger

interface InternalProperty<T> : Property<T> {

    val logger: Logger
    val propertyScope: PropertyScope

    /**
     * Tries to update the [value] of this [InternalProperty] to [newValue] using [event]
     * to check for circular dependencies. This function is only for internal usage.
     * @return `true` if change happened, `false` if not
     */
    fun updateWithEvent(
        oldValue: T,
        newValue: T,
        event: ObservableValueChanged<*>
    ): Boolean

    companion object
}
