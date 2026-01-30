package org.hexworks.cobalt.databinding.internal.binding

import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.behavior.DisposeState
import org.hexworks.cobalt.core.api.behavior.NotDisposed
import org.hexworks.cobalt.databinding.internal.Cobalt
import org.hexworks.cobalt.databinding.api.binding.Binding
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.databinding.api.extension.disposeSubscriptions
import org.hexworks.cobalt.databinding.api.extension.toInternalProperty
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.event.PropertyScope
import org.hexworks.cobalt.events.api.Subscription
import org.hexworks.cobalt.events.api.simpleSubscribeTo

/**
 * A [ComputedDualBinding] creates a [Binding] using **two** [ObservableValue]s which will get
 * updated whenever any of those values get updated using [computerFn] to compute the new value
 * of this [Binding].
 */
internal class ComputedDualBinding<out S0, out S1, T>(
    private val source0: ObservableValue<S0>,
    private val source1: ObservableValue<S1>,
    private val computerFn: (S0, S1) -> T
) : Binding<T> {

    override val name = "ComputedDualBinding"

    private val target = computerFn(source0.value, source1.value).toInternalProperty()

    override val value: T
        get() {
            require(disposed.not()) {
                "Can't calculate the value of a Binding which is disposed."
            }
            return target.value
        }

    override var disposeState: DisposeState =
        NotDisposed
        internal set

    override val id = UUID.randomUUID()

    private val propertyScope = PropertyScope(id)
    private val subscriptions = mutableListOf<Subscription>()

    init {
        subscriptions.add(
            source0.onChange { event ->
                val oldValue = computerFn(event.oldValue, source1.value)
                val newValue = computerFn(event.newValue, source1.value)
                if (oldValue != newValue) {
                    target.value = newValue
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
        )
        subscriptions.add(
            source1.onChange { event ->
                val oldValue = computerFn(source0.value, event.oldValue)
                val newValue = computerFn(source0.value, event.newValue)
                if (oldValue != newValue) {
                    target.value = newValue
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
        )
    }

    override fun dispose(disposeState: DisposeState) {
        this.disposeState = disposeState
        Cobalt.eventbus.cancelScope(propertyScope)
        subscriptions.disposeSubscriptions()
    }

    override fun onChange(fn: (ObservableValueChanged<T>) -> Unit): Subscription {
        return Cobalt.eventbus.simpleSubscribeTo<ObservableValueChanged<T>>(
            ObservableValueChanged.unsafeCast(),
            propertyScope
        ) {
            fn(it)
        }
    }

    companion object
}
