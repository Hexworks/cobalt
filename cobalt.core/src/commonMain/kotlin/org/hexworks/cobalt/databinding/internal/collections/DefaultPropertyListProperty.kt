@file:Suppress("UNUSED_PARAMETER")

package org.hexworks.cobalt.databinding.internal.collections

import kotlinx.collections.immutable.PersistentList
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.extensions.identity
import org.hexworks.cobalt.databinding.api.collection.ListProperty
import org.hexworks.cobalt.databinding.api.event.*
import org.hexworks.cobalt.databinding.api.property.PropertyValidator
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.property.base.BaseProperty
import org.hexworks.cobalt.events.api.Subscription

@Suppress("UNCHECKED_CAST")
internal class DefaultPropertyListProperty<T, P : ObservableValue<T>>(
    initialValue: PersistentList<P>,
    optionalName: String?,
    validator: PropertyValidator<List<P>>
) : BaseProperty<PersistentList<P>>(
    initialValue = initialValue,
    name = optionalName ?: "DefaultPropertyListProperty",
    validator = validator
),
    ListProperty<P>,
    List<P> {

    private val uniqueProperties = mutableMapOf<UUID, Pair<P, Subscription>>()

    override val size: Int
        get() = value.size

    init {
        value.forEach {
            it.subscribeToChanges()
        }
    }

    override fun contains(element: P) = value.contains(element)

    override fun containsAll(elements: Collection<P>) = value.containsAll(elements)

    override fun isEmpty() = value.isEmpty()

    override fun iterator() = value.iterator()

    override fun get(index: Int) = value[index]

    override fun indexOf(element: P) = value.indexOf(element)

    override fun lastIndexOf(element: P) = value.lastIndexOf(element)

    override fun listIterator() = value.listIterator()

    override fun listIterator(index: Int) = value.listIterator(index)

    override fun builder(): PersistentList.Builder<P> = value.builder()

    // MUTATORS

    override fun add(element: P): PersistentList<P> {
        element.subscribeToChanges()
        return updateCurrentValue(ListAdd(element)) { it.add(element) }
    }


    override fun add(index: Int, element: P): PersistentList<P> {
        element.subscribeToChanges()
        return updateCurrentValue(ListAddAt(index, element)) { it.add(index, element) }
    }


    override fun addAll(elements: Collection<P>): PersistentList<P> {
        elements.forEach { it.subscribeToChanges() }
        return updateCurrentValue(ListAddAll(elements)) { it.addAll(elements) }
    }


    override fun addAll(index: Int, c: Collection<P>): PersistentList<P> {
        c.forEach { it.subscribeToChanges() }
        return updateCurrentValue(ListAddAllAt(index, c)) { it.addAll(index, c) }
    }


    override fun set(index: Int, element: P): PersistentList<P> {
        if (value.size > index) {
            get(index).unsubscribeFromChanges()
            element.subscribeToChanges()
        }
        return updateCurrentValue(ListSet(index, element)) { it.set(index, element) }
    }


    override fun remove(element: P): PersistentList<P> {
        element.unsubscribeFromChanges()
        return updateCurrentValue(ListRemove(element)) { it.remove(element) }
    }


    override fun removeAt(index: Int): PersistentList<P> {
        if (value.size > index) {
            value[index].unsubscribeFromChanges()
        }
        return updateCurrentValue(ListRemoveAt(index)) { it.removeAt(index) }
    }


    override fun removeAll(elements: Collection<P>): PersistentList<P> {
        elements.forEach { it.unsubscribeFromChanges() }
        return updateCurrentValue(ListRemoveAll(elements)) { it.removeAll(elements) }
    }


    override fun removeAll(predicate: (P) -> Boolean): PersistentList<P> {
        value.filter(predicate).forEach { it.unsubscribeFromChanges() }
        return updateCurrentValue(ListRemoveAllWhen(predicate)) { it.removeAll(predicate) }
    }


    override fun retainAll(elements: Collection<P>): PersistentList<P> {
        value.minus(elements).forEach { it.unsubscribeFromChanges() }
        return updateCurrentValue(ListRetainAll(elements)) { it.retainAll(elements) }
    }


    override fun clear(): PersistentList<P> {
        value.forEach { it.unsubscribeFromChanges() }
        if (uniqueProperties.isNotEmpty()) {
            logger.warn {
                """
                    There are still remaining property subscriptions after clearing the list.
                    If you see this it means that there is a bug. Please contact the developers
                    """
            }
        }
        return updateCurrentValue(ListClear) { it.clear() }
    }

    private fun P.subscribeToChanges() {
        val p = this
        if (uniqueProperties.containsKey(p.id).not()) {
            uniqueProperties[p.id] = this to onChange { change ->
                updateCurrentValue(ListPropertyChange(change), identity())
            }
        }
    }

    private fun P.unsubscribeFromChanges() {
        val p = this
        uniqueProperties.remove(p.id)?.second?.dispose()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as DefaultPropertyListProperty<*, *>

        return value.map { it.value } == other.value.map { it.value }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    companion object
}
