@file:Suppress("UNUSED_PARAMETER")

package org.hexworks.cobalt.databinding.internal.collections

import kotlinx.collections.immutable.PersistentList
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.behavior.DisposeState
import org.hexworks.cobalt.databinding.api.binding.Binding
import org.hexworks.cobalt.databinding.api.collection.ObservableListBinding
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.events.api.Subscription

@Suppress("UNCHECKED_CAST")
internal class ListBindingDecorator<T>(
    private val binding: Binding<PersistentList<T>>,
    optionalName: String? = null,
) : ObservableListBinding<T> {

    override val id: UUID
        get() = binding.id

    override val value: PersistentList<T>
        get() = binding.value

    override val size: Int
        get() = value.size

    override val name = optionalName ?: "ListBindingDecorator"

    override fun onChange(fn: (ObservableValueChanged<PersistentList<T>>) -> Unit): Subscription {
        return binding.onChange(fn)
    }

    override operator fun contains(element: T) = value.contains(element)

    override fun containsAll(elements: Collection<T>) = value.containsAll(elements)

    override operator fun get(index: Int) = value[index]

    override fun indexOf(element: T) = value.indexOf(element)

    override fun isEmpty() = value.isEmpty()

    override fun iterator() = value.iterator()

    override fun lastIndexOf(element: T) = value.lastIndexOf(element)

    override fun listIterator() = value.listIterator()

    override fun listIterator(index: Int) = value.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = value.subList(fromIndex, toIndex)

    override fun add(element: T): PersistentList<T> {
        return value.add(element)
    }

    override fun remove(element: T): PersistentList<T> {
        return value.remove(element)
    }

    override fun addAll(elements: Collection<T>): PersistentList<T> {
        return value.addAll(elements)
    }

    override fun addAll(index: Int, c: Collection<T>): PersistentList<T> {
        return value.addAll(index, c)
    }

    override fun removeAll(elements: Collection<T>): PersistentList<T> {
        return value.removeAll(elements)
    }

    override fun removeAll(predicate: (T) -> Boolean): PersistentList<T> {
        return value.removeAll(predicate)
    }

    override fun retainAll(elements: Collection<T>): PersistentList<T> {
        return value.retainAll(elements)
    }

    override fun clear(): PersistentList<T> {
        return value.clear()
    }

    override fun set(index: Int, element: T): PersistentList<T> {
        return value.set(index, element)
    }

    override fun add(index: Int, element: T): PersistentList<T> {
        return value.add(index, element)
    }

    override fun removeAt(index: Int): PersistentList<T> {
        return value.removeAt(index)
    }

    override fun builder() = value.builder()

    override val disposeState: DisposeState
        get() = binding.disposeState

    override fun dispose(disposeState: DisposeState) = binding.dispose(disposeState)

    companion object
}
