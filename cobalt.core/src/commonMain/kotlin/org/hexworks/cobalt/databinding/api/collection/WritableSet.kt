package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentSet
import org.hexworks.cobalt.databinding.api.value.Value

/**
 * A [WritableSet] is a [Value] that wraps an underlying [PersistentSet]
 * and allows changing its [value].
 */
interface WritableSet<T> : WritableCollection<T, PersistentSet<T>> {

    companion object
}
