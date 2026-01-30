package org.hexworks.cobalt.databinding.internal.binding

import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.behavior.DisposeState
import org.hexworks.cobalt.core.api.behavior.NotDisposed
import org.hexworks.cobalt.databinding.internal.Cobalt
import org.hexworks.cobalt.databinding.api.binding.Binding
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.databinding.api.extension.disposeSubscriptions
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.event.PropertyScope
import org.hexworks.cobalt.databinding.internal.property.InternalProperty
import org.hexworks.cobalt.events.api.Subscription
import org.hexworks.cobalt.events.api.simpleSubscribeTo
import org.hexworks.cobalt.logging.api.LoggerFactory

abstract class BaseBinding<S, T>(
    internal val source: ObservableValue<S>,
    internal val target: InternalProperty<T>,
    internal val subscriptions: MutableList<Subscription>
) : Binding<T> {

    override val value: T
        get() {
            require(disposed.not()) {
                "Can't calculate the value of a Binding which is disposed."
            }
            return target.value
        }

    override var disposeState: DisposeState = NotDisposed
        internal set

    final override val id = UUID.randomUUID()

    internal val propertyScope = PropertyScope(id)

    override fun dispose(disposeState: DisposeState) {
        this.disposeState = disposeState
        Cobalt.eventbus.cancelScope(propertyScope)
        subscriptions.disposeSubscriptions()
    }

    @Suppress("UNCHECKED_CAST")
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
