package org.hexworks.cobalt.databinding.api.collection

import kotlinx.collections.immutable.PersistentList
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.api.value.WritableValue

/**
 * A [ListProperty] is a [Property] that wraps an underlying [PersistentList].
 * @see Property
 * @see ObservableValue
 * @see WritableValue
 */
interface ListProperty<T> :
    ObservableList<T>,
    WritableList<T>,
    ObservableCollection<T, PersistentList<T>>,
    Property<PersistentList<T>> {

    companion object
}
