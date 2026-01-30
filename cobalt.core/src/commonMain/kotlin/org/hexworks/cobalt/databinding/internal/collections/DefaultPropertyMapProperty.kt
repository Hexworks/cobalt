@file:Suppress("UNUSED_PARAMETER")

package org.hexworks.cobalt.databinding.internal.collections

import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.extensions.identity
import org.hexworks.cobalt.databinding.api.collection.MapProperty
import org.hexworks.cobalt.databinding.api.event.ListPropertyChange
import org.hexworks.cobalt.databinding.api.event.MapClear
import org.hexworks.cobalt.databinding.api.event.MapPut
import org.hexworks.cobalt.databinding.api.event.MapPutAll
import org.hexworks.cobalt.databinding.api.event.MapRemove
import org.hexworks.cobalt.databinding.api.event.MapRemoveWithValue
import org.hexworks.cobalt.databinding.api.property.PropertyValidator
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.property.base.BaseProperty
import org.hexworks.cobalt.events.api.Subscription

@Suppress("UNCHECKED_CAST")
internal class DefaultPropertyMapProperty<K : Any, V, P : ObservableValue<V>>(
    initialValue: PersistentMap<K, P>,
    optionalName: String?,
    validator: PropertyValidator<PersistentMap<K, P>>
) : BaseProperty<PersistentMap<K, P>>(
    initialValue = initialValue,
    name = optionalName ?: "DefaultPropertyMapProperty",
    validator = validator
),
    MapProperty<K, P> {

    private val uniqueProperties = mutableMapOf<UUID, Pair<P, Subscription>>()

    override val size: Int
        get() = value.size

    init {
        value.values.forEach {
            it.subscribeToChanges()
        }
    }

    override fun containsKey(key: K) = value.containsKey(key)

    override fun containsValue(value: P) = this.value.containsValue(value)

    override fun get(key: K): P? = value.get(key)

    override fun isEmpty() = value.isEmpty()

    override val entries: ImmutableSet<Map.Entry<K, P>>
        get() = value.entries
    override val keys: ImmutableSet<K>
        get() = value.keys
    override val values: ImmutableCollection<P>
        get() = value.values

    override fun builder() = value.builder()

    override fun put(key: K, value: P): PersistentMap<K, P> {
        value.subscribeToChanges()
        return updateCurrentValue(MapPut(key, value)) { it.put(key, value) }
    }

    override fun putAll(m: Map<out K, P>): PersistentMap<K, P> {
        m.values.forEach { it.subscribeToChanges() }
        return updateCurrentValue(MapPutAll(m)) { it.putAll(m) }
    }

    override fun remove(key: K): PersistentMap<K, P> {
        value[key]?.unsubscribeFromChanges()
        return updateCurrentValue(MapRemove(key)) { it.remove(key) }
    }

    override fun remove(key: K, value: P): PersistentMap<K, P> {
        this.value[key]?.unsubscribeFromChanges()
        return updateCurrentValue(MapRemoveWithValue(key, value)) { it.remove(key, value) }
    }

    override fun clear(): PersistentMap<K, P> {
        value.values.forEach { it.unsubscribeFromChanges() }
        return updateCurrentValue(MapClear) { it.clear() }
    }

    private fun P.subscribeToChanges() {
        val p = this
        if (uniqueProperties.containsKey(p.id).not()) {
            uniqueProperties[p.id] = this to onChange { ovc ->
                updateCurrentValue(ListPropertyChange(ovc), identity())
            }
        }
    }

    private fun P.unsubscribeFromChanges() {
        val p = this
        uniqueProperties.remove(p.id)?.second?.dispose()
    }

    companion object
}
