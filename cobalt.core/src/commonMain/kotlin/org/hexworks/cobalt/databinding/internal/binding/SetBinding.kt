package org.hexworks.cobalt.databinding.internal.binding

import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import org.hexworks.cobalt.databinding.internal.Cobalt
import org.hexworks.cobalt.databinding.api.collection.ObservableSet
import org.hexworks.cobalt.databinding.api.event.ObservableValueChanged
import org.hexworks.cobalt.databinding.api.event.SetAdd
import org.hexworks.cobalt.databinding.api.event.SetAddAll
import org.hexworks.cobalt.databinding.api.event.SetChange
import org.hexworks.cobalt.databinding.api.event.SetClear
import org.hexworks.cobalt.databinding.api.event.SetPropertyChange
import org.hexworks.cobalt.databinding.api.event.SetRemove
import org.hexworks.cobalt.databinding.api.event.SetRemoveAll
import org.hexworks.cobalt.databinding.api.event.SetRemoveAllWhen
import org.hexworks.cobalt.databinding.api.event.SetRetainAll
import org.hexworks.cobalt.databinding.api.extension.toProperty
import org.hexworks.cobalt.databinding.api.value.ValueValidationResult
import org.hexworks.cobalt.databinding.internal.exception.CircularBindingException
import org.hexworks.cobalt.databinding.internal.extensions.asInternalProperty

@Suppress("UNCHECKED_CAST")
class SetBinding<S, T>(
    source: ObservableSet<S>,
    converter: (S) -> T
) : BaseBinding<PersistentSet<S>, PersistentSet<T>>(
    source = source,
    target = persistentSetOf<T>().toProperty().asInternalProperty().apply {
        this.transformValue { it.addAll(source.map(converter)) }
    },
    subscriptions = mutableListOf()
) {

    override val name: String = "SetBinding"

    init {
        subscriptions.add(
            source.onChange { event ->
                if (event.trace.any { it.emitter == this }) {
                    throw CircularBindingException(
                        "Circular binding detected with trace ${event.trace.joinToString()} for property $this. Loop was prevented."
                    )
                }
                val type = event.type
                val oldValue = target.value
                val maybeNewValue: ValueValidationResult<PersistentSet<T>> = target.transformValue {
                    // This is not nice...but the thing is that in order to make ObservableList only emit ListChanges
                    // in a type safe way we'd have to add it as a type parameter to the whole property abstraction
                    // ...which would also break the API
                    (type as? SetChange)?.let {
                        when (type) {
                            is SetAdd<*> -> oldValue.add(converter(type.element as S))
                            is SetRemove<*> -> oldValue.remove(converter(type.element as S))
                            is SetAddAll<*> -> oldValue.addAll(type.elements.map { converter(it as S) })
                            is SetRemoveAll<*> -> oldValue.removeAll(type.elements.map { converter(it as S) })
                            is SetRemoveAllWhen<*> -> oldValue.removeAll {
                                (type.predicate as (T) -> Boolean)(it)
                            }
                            is SetRetainAll<*> -> oldValue.retainAll(type.elements.map { converter(it as S) })
                            SetClear -> oldValue.clear()
                            is SetPropertyChange<*> -> oldValue
                        }
                    } ?: oldValue
                }
                if (maybeNewValue.successful && oldValue != maybeNewValue.value) {
                    Cobalt.eventbus.publish(
                        event = ObservableValueChanged(
                            oldValue = oldValue,
                            newValue = maybeNewValue.value,
                            observableValue = this,
                            emitter = this,
                            type = event.type,
                            trace = listOf(event) + event.trace
                        ),
                        eventScope = propertyScope
                    )
                }
            }
        )
    }

    override fun toString() = "${this::class.simpleName}(source=$source, target=$target)"

    companion object
}
