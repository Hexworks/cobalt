package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentMap
import org.hexworks.cobalt.databinding.api.value.ObservableValue

/**
 * An [ObservableMap] is an [ObservableValue] that wraps an underlying
 * [PersistentMap] and can be used to track its changes with [onChange].
 */
interface ObservableMap<K : Any, V> : PersistentMap<K, V>, ObservableValue<PersistentMap<K, V>> {

    companion object
}
