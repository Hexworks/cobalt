package org.hexworks.cobalt.databinding.api.value

import org.hexworks.cobalt.databinding.api.binding.Binding

/**
 * A [WritableValue] wraps a value that can be both *read* and *written*.
 */
interface WritableValue<T> : Value<T> {

    override var value: T

    /**
     * Tries to update the [value] with the given [newValue].
     * @return [ValueValidationSuccessful] if [newValue] is
     * acceptable, [ValueValidationFailed] if not.
     */
    fun updateValue(newValue: T): ValueValidationResult<T>

    /**
     * Tries to transform the [value] with the given [transformer] function.
     * It is guaranteed that no concurrent changes will happen to this [WritableValue]
     * while being transformed.
     * @return [ValueValidationSuccessful] if the new value is
     * acceptable, [ValueValidationFailed] if not.
     */
    fun transformValue(transformer: (T) -> T): ValueValidationResult<T>

    /**
     * Starts updating this [WritableValue] from the given [observable]. If [bindingAction]
     * is [UpdateOnBind] then the value of this [WritableValue] will be updated
     * when the binding takes place.
     * Otherwise, it will only get updated when [observable] is updated.
     * @return a [Binding] which can be disposed to stop the updates
     */
    fun updateFrom(
        observable: ObservableValue<T>,
        bindingAction: BindingAction = UpdateOnBind
    ): Binding<T>

    /**
     * Starts updating this [WritableValue] from the given [observable].
     * Uses the given [converter] to convert the values. If [bindingAction]
     * is [UpdateOnBind] then the value of this [WritableValue] will be updated
     * when the binding takes place. Otherwise, it will only get updated when
     * [observable] is updated.
     * @return a [Binding] which can be disposed to stop the updates
     */
    fun <S> updateFrom(
        observable: ObservableValue<S>,
        bindingAction: BindingAction = UpdateOnBind,
        converter: (S) -> T
    ): Binding<T>

    companion object
}
