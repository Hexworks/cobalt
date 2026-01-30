package org.hexworks.cobalt.databinding.api.property

import org.hexworks.cobalt.databinding.api.binding.Binding
import org.hexworks.cobalt.databinding.api.converter.IsomorphicConverter
import org.hexworks.cobalt.databinding.api.value.BindingAction
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.api.value.UpdateOnBind
import org.hexworks.cobalt.databinding.api.value.Value
import org.hexworks.cobalt.databinding.api.value.WritableValue
import org.hexworks.cobalt.databinding.internal.property.DefaultPropertyDelegate

/**
 * A [Property] is a [Value] that can be *read*, *written* and *observed*
 * and also supports *bidirectional* binding using the [bind] function.
 * @see WritableValue
 * @see ObservableValue
 */
interface Property<T> : WritableValue<T>, ObservableValue<T> {

    /**
     * Creates a bidirectional binding between this [Property] and [other].
     * eg: whenever one is updated, the other will get updated with
     * the new value.
     * If [bindingAction] is [UpdateOnBind] then the value of this
     * [Property] will be updated when the binding takes place.
     * Otherwise, it will only get updated on [other]'s next update.
     */
    fun bind(
        other: Property<T>,
        bindingAction: BindingAction = UpdateOnBind
    ): Binding<T>

    /**
     * Creates a bidirectional binding between this [Property] and [other].
     * Uses the given [IsomorphicConverter] to convert the values between
     * the subject properties.
     *
     * If [bindingAction] is [UpdateOnBind] then the value of this
     * [Property] will be updated when the binding takes place.
     * Otherwise, it will only get updated on [other]'s next update.
     */
    fun <S> bind(
        other: Property<S>,
        bindingAction: BindingAction = UpdateOnBind,
        converter: IsomorphicConverter<S, T>
    ): Binding<T>

    /**
     * Creates a [PropertyDelegate] for this [Property].
     */
    fun asDelegate(): PropertyDelegate<T> = DefaultPropertyDelegate(this)

    companion object
}
