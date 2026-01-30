package org.hexworks.cobalt.databinding.internal.binding

import org.hexworks.cobalt.databinding.internal.Cobalt
import org.hexworks.cobalt.databinding.api.binding.Binding
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.databinding.api.event.ScalarChange
import org.hexworks.cobalt.databinding.api.extension.toProperty
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.property.InternalProperty

/**
 * A [ComputedBinding] creates a [Binding] using an [ObservableValue]. The [Binding] will get
 * updated whenever the [ObservableValue] changes using [converter] to compute the new value of this [Binding].
 */
@Suppress("UNCHECKED_CAST")
class ComputedBinding<S, T>(
    source: ObservableValue<S>,
    converter: (S) -> T
) : BaseBinding<S, T>(
    source = source,
    target = converter(source.value).toProperty() as InternalProperty<T>,
    subscriptions = mutableListOf()
) {

    override val name = "ComputedBinding"

    init {
        subscriptions.add(source.onChange { event ->
            val oldValue = converter(event.oldValue)
            val newValue = converter(event.newValue)
            if (target.updateWithEvent( // TODO: we send 2 events, do we need to call this?
                    oldValue = oldValue,
                    newValue = newValue,
                    event = event
                )
            ) {
                Cobalt.eventbus.publish(
                    event = ObservableValueChanged(
                        oldValue = oldValue,
                        newValue = newValue,
                        observableValue = this,
                        emitter = this,
                        trace = listOf(event) + event.trace,
                        type = ScalarChange
                    ),
                    eventScope = propertyScope
                )
            }
        })
    }

    companion object
}
