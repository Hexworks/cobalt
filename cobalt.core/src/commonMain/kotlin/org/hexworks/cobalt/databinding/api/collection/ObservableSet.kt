package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import org.hexworks.cobalt.databinding.api.value.ObservableValue

/**
 * An [ObservableSet] is an [ObservableValue] that wraps an underlying
 * [PersistentList] and can be used to track its changes with [onChange].
 */
interface ObservableSet<T> : ObservableCollection<T, PersistentSet<T>>, PersistentSet<T> {

    companion object
}
