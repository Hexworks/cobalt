package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentList
import org.hexworks.cobalt.databinding.api.value.ObservableValue

/**
 * An [ObservableList] is an [ObservableValue] that wraps an underlying
 * [PersistentList] and can be used to track its changes with [onChange].
 */
interface ObservableList<T> :
    ObservableCollection<T, PersistentList<T>>,
    ObservableValue<PersistentList<T>>,
    PersistentList<T> {

    companion object
}
