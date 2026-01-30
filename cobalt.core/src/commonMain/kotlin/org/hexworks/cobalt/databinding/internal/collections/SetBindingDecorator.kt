@file:Suppress("UNUSED_PARAMETER")

package org.hexworks.cobalt.databinding.internal.collections

import kotlinx.collections.immutable.PersistentSet
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.behavior.DisposeState
import org.hexworks.cobalt.databinding.api.binding.Binding
import org.hexworks.cobalt.databinding.api.collection.ObservableSetBinding
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.events.api.Subscription

@Suppress("UNCHECKED_CAST")
internal class SetBindingDecorator<T>(
    private val binding: Binding<PersistentSet<T>>,
    optionalName: String? = null,
) : ObservableSetBinding<T> {

    override val id: UUID
        get() = binding.id

    override val value: PersistentSet<T>
        get() = binding.value

    override val size: Int
        get() = value.size

    override val name = optionalName ?: "SetBindingDecorator"

    override fun onChange(fn: (ObservableValueChanged<PersistentSet<T>>) -> Unit): Subscription {
        return binding.onChange(fn)
    }

    override fun contains(element: T) = value.contains(element)

    override fun containsAll(elements: Collection<T>) = value.containsAll(elements)

    override fun isEmpty() = value.isEmpty()

    override fun iterator() = value.iterator()

    override fun add(element: T): PersistentSet<T> {
        return value.add(element)
    }

    override fun remove(element: T): PersistentSet<T> {
        return value.remove(element)
    }

    override fun addAll(elements: Collection<T>): PersistentSet<T> {
        return value.addAll(elements)
    }

    override fun removeAll(elements: Collection<T>): PersistentSet<T> {
        return value.removeAll(elements)
    }

    override fun removeAll(predicate: (T) -> Boolean): PersistentSet<T> {
        return value.removeAll(predicate)
    }

    override fun retainAll(elements: Collection<T>): PersistentSet<T> {
        return value.retainAll(elements)
    }

    override fun clear(): PersistentSet<T> {
        return value.clear()
    }

    override fun builder() = value.builder()

    override val disposeState: DisposeState
        get() = binding.disposeState

    override fun dispose(disposeState: DisposeState) = binding.dispose(disposeState)

    companion object
}
