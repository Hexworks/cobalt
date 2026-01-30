package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentList
import org.hexworks.cobalt.databinding.api.value.Value

/**
 * A [WritableList] is a [Value] that wraps an underlying [PersistentList]
 * and allows changing its [value].
 */
interface WritableList<T> : WritableCollection<T, PersistentList<T>> {

    companion object
}
