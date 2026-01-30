package org.hexworks.cobalt.databinding.api.value

import org.hexworks.cobalt.databinding.api.property.Property

/**
 * [Value] is an abstraction which can be used to wrap an arbitrary [value].
 * It serves as the base abstraction for [ObservableValue] and [WritableValue]
 * which together form the [Property] abstraction.
 */
interface Value<out T> {

    val value: T

    companion object
}
