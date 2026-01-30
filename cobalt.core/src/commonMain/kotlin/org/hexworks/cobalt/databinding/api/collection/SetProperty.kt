package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentSet
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.api.value.WritableValue

/**
 * A [SetProperty] is a [Property] that wraps an underlying [PersistentSet].
 * @see Property
 * @see ObservableValue
 * @see WritableValue
 */
interface SetProperty<T> :
    ObservableSet<T>,
    WritableSet<T>,
    ObservableCollection<T, PersistentSet<T>>,
    Property<PersistentSet<T>> {

    companion object
}
