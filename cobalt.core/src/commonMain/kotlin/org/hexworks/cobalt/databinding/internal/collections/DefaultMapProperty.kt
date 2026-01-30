@file:Suppress("UNUSED_PARAMETER")

package org.hexworks.cobalt.databinding.internal.collections

import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import org.hexworks.cobalt.databinding.api.collection.MapProperty
import org.hexworks.cobalt.databinding.api.event.*
import org.hexworks.cobalt.databinding.api.property.PropertyValidator
import org.hexworks.cobalt.databinding.internal.property.base.BaseProperty

@Suppress("UNCHECKED_CAST")
class DefaultMapProperty<K : Any, V>(
    initialValue: PersistentMap<K, V>,
    optionalName: String?,
    validator: PropertyValidator<PersistentMap<K, V>>
) : BaseProperty<PersistentMap<K, V>>(
    initialValue = initialValue,
    name = optionalName ?: "DefaultMapProperty",
    validator = validator
), MapProperty<K, V> {

    override val entries: ImmutableSet<Map.Entry<K, V>>
        get() = value.entries

    override val keys: ImmutableSet<K>
        get() = value.keys

    override val size: Int
        get() = value.size

    override val values: ImmutableCollection<V>
        get() = value.values

    override fun containsKey(key: K) = value.containsKey(key)

    override fun containsValue(value: V) = this.value.containsValue(value)

    override fun get(key: K): V? = value[key]

    override fun isEmpty() = value.isEmpty()

    override fun builder() = value.builder()

    override fun put(key: K, value: V): PersistentMap<K, V> {
        return updateCurrentValue(MapPut(key, value)) { it.put(key, value) }
    }

    override fun putAll(m: Map<out K, V>): PersistentMap<K, V> {
        return updateCurrentValue(MapPutAll(m)) { it.putAll(m) }
    }

    override fun remove(key: K): PersistentMap<K, V> {
        return updateCurrentValue(MapRemove(key)) { it.remove(key) }
    }

    override fun remove(key: K, value: V): PersistentMap<K, V> {
        return updateCurrentValue(MapRemoveWithValue(key, value)) { it.remove(key, value) }
    }

    override fun clear(): PersistentMap<K, V> {
        return updateCurrentValue(MapClear) { it.clear() }
    }

    companion object
}
