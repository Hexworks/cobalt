package org.hexworks.cobalt.databinding.api.value

import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.events.api.EventSource
import org.hexworks.cobalt.events.api.Subscription

/**
 * An [ObservableValue] wraps a value and allows to observe the value for changes.
 */
interface ObservableValue<out T> : Value<T>, EventSource {

    /**
     * Observability makes a value an event source, so it has to have
     * a unique identifier
     */
    override val id: UUID
    val name: String

    /**
     * Starts observing this [ObservableValue] for changes. If it happens
     * [fn] will be called. **Note that** if the [value] of this [ObservableValue]
     * is **set** to the same value, the callback won't be called.
     */
    fun onChange(fn: (ObservableValueChanged<T>) -> Unit): Subscription

    companion object
}
