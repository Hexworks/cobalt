package org.hexworks.cobalt.databinding.internal.binding

import org.hexworks.cobalt.databinding.internal.Cobalt
import org.hexworks.cobalt.databinding.api.binding.Binding
import org.hexworks.cobalt.databinding.api.converter.IsomorphicConverter
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.databinding.internal.extensions.runWithDisposeOnFailure
import org.hexworks.cobalt.databinding.internal.property.InternalProperty

/**
 * Creates a **bidirectional** [Binding] between [source] and [target] which means that
 * whenever [source] gets updated [target] will be updated as well with the new value
 * and vice versa.
 * [converter] will be used to convert the values between [source] and [target].
 */
internal class BidirectionalBinding<S, T>(
    source: InternalProperty<S>,
    target: InternalProperty<T>,
    converter: IsomorphicConverter<S, T>
) : BaseBinding<S, T>(
    source = source,
    target = target,
    subscriptions = mutableListOf()
) {

    override val name = "BidirectionalBinding"

    private val reverseConverter = converter.reverseConverter()

    init {
        subscriptions.add(
            source.onChange { event ->
                runWithDisposeOnFailure {
                    val oldValue = converter.convert(event.oldValue)
                    val newValue = converter.convert(event.newValue)
                    if (target.updateWithEvent(
                            oldValue = oldValue,
                            newValue = newValue,
                            event = event
                        )
                    ) {
                        Cobalt.eventbus.publish(
                            event = ObservableValueChanged<Any?>(
                                oldValue = oldValue,
                                newValue = newValue,
                                observableValue = this,
                                emitter = this,
                                trace = listOf(event) + event.trace,
                                type = event.type
                            ),
                            eventScope = propertyScope
                        )
                    }
                }
            }
        )
        subscriptions.add(
            target.onChange { event ->
                runWithDisposeOnFailure {
                    val oldValue = reverseConverter.convert(event.oldValue)
                    val newValue = reverseConverter.convert(event.newValue)
                    if (source.updateWithEvent(
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
                                type = event.type
                            ),
                            eventScope = propertyScope
                        )
                    }
                }
            }
        )
    }

    companion object
}
