package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import org.hexworks.cobalt.databinding.api.value.Value
import org.hexworks.cobalt.databinding.api.value.WritableValue

/**
 * A [WritableMap] is a [Value] that wraps an underlying [PersistentList]
 * and allows changing its [value].
 */
interface WritableMap<K : Any, V> : PersistentMap<K, V>, WritableValue<PersistentMap<K, V>> {

    companion object
}
