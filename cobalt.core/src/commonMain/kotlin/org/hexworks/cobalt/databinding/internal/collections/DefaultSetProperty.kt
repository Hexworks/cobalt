@file:Suppress("UNUSED_PARAMETER")

package org.hexworks.cobalt.databinding.internal.collections

import kotlinx.collections.immutable.PersistentSet
import org.hexworks.cobalt.databinding.api.collection.SetProperty
import org.hexworks.cobalt.databinding.api.event.*
import org.hexworks.cobalt.databinding.api.property.PropertyValidator
import org.hexworks.cobalt.databinding.internal.property.base.BaseProperty

@Suppress("UNCHECKED_CAST")
class DefaultSetProperty<T>(
    initialValue: PersistentSet<T>,
    optionalName: String?,
    validator: PropertyValidator<Set<T>>
) : BaseProperty<PersistentSet<T>>(
    initialValue = initialValue,
    name = optionalName ?: "DefaultSetProperty",
    validator = validator
), SetProperty<T> {

    override val size: Int
        get() = value.size

    override fun contains(element: T) = value.contains(element)

    override fun containsAll(elements: Collection<T>) = value.containsAll(elements)

    override fun isEmpty() = value.isEmpty()

    override fun iterator() = value.iterator()

    override fun builder() = value.builder()

    override fun add(element: T): PersistentSet<T> {
        return updateCurrentValue(SetAdd(element)) { it.add(element) }
    }

    override fun addAll(elements: Collection<T>): PersistentSet<T> {
        return updateCurrentValue(SetAddAll(elements)) { it.addAll(elements) }
    }

    override fun remove(element: T): PersistentSet<T> {
        return updateCurrentValue(SetRemove(element)) { it.remove(element) }
    }

    override fun removeAll(predicate: (T) -> Boolean): PersistentSet<T> {
        return updateCurrentValue(SetRemoveAllWhen(predicate)) { it.removeAll(predicate) }
    }

    override fun removeAll(elements: Collection<T>): PersistentSet<T> {
        return updateCurrentValue(SetRemoveAll(elements)) { it.removeAll(elements) }
    }

    override fun retainAll(elements: Collection<T>): PersistentSet<T> {
        return updateCurrentValue(SetRetainAll(elements)) { it.retainAll(elements) }
    }

    override fun clear(): PersistentSet<T> {
        return updateCurrentValue(SetClear) { it.clear() }
    }

    companion object
}
