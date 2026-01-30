package org.hexworks.cobalt.databinding.api.value

/**
 * Represents the possible actions that can be performed
 * when an updating binding is created for a [WritableValue].
 * eg: `writableValue.updateFrom` is called.
 */
sealed class BindingAction {
    companion object
}

/**
 * Update the [WritableValue] with the value of the
 * [ObservableValue] when the binding is created.
 */
object UpdateOnBind : BindingAction()

/**
 * Do nothing when the binding is created.
 */
object NoActionOnBind : BindingAction()