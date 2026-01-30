@file:Suppress("UNUSED_PARAMETER")

package org.hexworks.cobalt.databinding.internal.collections

import kotlinx.collections.immutable.PersistentSet
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.extensions.identity
import org.hexworks.cobalt.databinding.api.collection.SetProperty
import org.hexworks.cobalt.databinding.api.event.*
import org.hexworks.cobalt.databinding.api.property.PropertyValidator
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.property.base.BaseProperty
import org.hexworks.cobalt.events.api.Subscription

@Suppress("UNCHECKED_CAST")
internal class DefaultPropertySetProperty<T, V : ObservableValue<T>>(
    initialValue: PersistentSet<V>,
    optionalName: String?,
    validator: PropertyValidator<PersistentSet<V>>
) : BaseProperty<PersistentSet<V>>(
    initialValue = initialValue,
    name = optionalName ?: "DefaultPropertySetProperty",
    validator = validator
),
    SetProperty<V> {

    private val uniqueProperties = mutableMapOf<UUID, Pair<V, Subscription>>()

    override val size: Int
        get() = value.size

    init {
        value.forEach {
            it.subscribeToChanges()
        }
    }

    override fun contains(element: V) = value.contains(element)

    override fun containsAll(elements: Collection<V>) = value.containsAll(elements)

    override fun isEmpty() = value.isEmpty()

    override fun iterator() = value.iterator()

    override fun builder() = value.builder()

    override fun add(element: V): PersistentSet<V> {
        element.subscribeToChanges()
        return updateCurrentValue(SetAdd(element)) { it.add(element) }
    }

    override fun addAll(elements: Collection<V>): PersistentSet<V> {
        elements.forEach { it.subscribeToChanges() }
        return updateCurrentValue(SetAddAll(elements)) { it.addAll(elements) }
    }

    override fun remove(element: V): PersistentSet<V> {
        element.unsubscribeFromChanges()
        return updateCurrentValue(SetRemove(element)) { it.remove(element) }
    }

    override fun removeAll(predicate: (V) -> Boolean): PersistentSet<V> {
        value.filter(predicate).forEach { it.unsubscribeFromChanges() }
        return updateCurrentValue(SetRemoveAllWhen(predicate)) { it.removeAll(predicate) }
    }

    override fun removeAll(elements: Collection<V>): PersistentSet<V> {
        elements.forEach { it.unsubscribeFromChanges() }
        return updateCurrentValue(SetRemoveAll(elements)) { it.removeAll(elements) }
    }

    override fun retainAll(elements: Collection<V>): PersistentSet<V> {
        value.minus(elements).forEach { it.unsubscribeFromChanges() }
        return updateCurrentValue(SetRetainAll(elements)) { it.retainAll(elements) }
    }

    override fun clear(): PersistentSet<V> {
        value.forEach { it.unsubscribeFromChanges() }
        if (uniqueProperties.isNotEmpty()) {
            logger.warn {
                """
                    There are still remaining property subscriptions after clearing the set.
                    If you see this it means that there is a bug. Please contact the developers
                    """
            }
        }
        return updateCurrentValue(SetClear) { it.clear() }
    }

    private fun V.subscribeToChanges() {
        val v = this
        if (uniqueProperties.containsKey(v.id).not()) {
            uniqueProperties[v.id] = this to onChange { ovc ->
                updateCurrentValue(SetPropertyChange(ovc), identity())
            }
        }
    }

    private fun V.unsubscribeFromChanges() {
        val v = this
        uniqueProperties.remove(v.id)?.second?.dispose()
    }

    companion object
}
