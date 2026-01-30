package org.hexworks.cobalt.databinding.api.binding

import org.hexworks.cobalt.core.api.behavior.Disposable
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.api.property.Property

/**
 * A [Binding] computes its value based on the value of its dependencies.
 * A binding is subscribed to the changes of its dependencies and updates
 * its value whenever any of them changes.
 * A [Binding] differs from a [Property] in that it is not mutable.
 * In other words, a [Binding] can be thought of as an *aggregation* of multiple
 * [ObservableValue]s.
 */
interface Binding<out T> : ObservableValue<T>, Disposable {

    companion object
}
