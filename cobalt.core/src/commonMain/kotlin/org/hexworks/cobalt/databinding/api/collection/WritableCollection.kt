package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentCollection
import org.hexworks.cobalt.databinding.api.value.Value
import org.hexworks.cobalt.databinding.api.value.WritableValue

/**
 * A [WritableCollection] is a [Value] that wraps an underlying [PersistentCollection]
 * and allows changing its [value].
 */
interface WritableCollection<T, C : PersistentCollection<T>> : WritableValue<C> {

    companion object
}
