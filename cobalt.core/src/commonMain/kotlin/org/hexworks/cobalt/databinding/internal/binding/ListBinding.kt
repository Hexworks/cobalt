package org.hexworks.cobalt.databinding.internal.binding

import kotlinx.collections.immutable.PersistentList
import org.hexworks.cobalt.databinding.internal.Cobalt
import org.hexworks.cobalt.databinding.api.collection.ObservableList
import org.hexworks.cobalt.databinding.api.event.*
import org.hexworks.cobalt.databinding.api.extension.map
import org.hexworks.cobalt.databinding.api.extension.toProperty
import org.hexworks.cobalt.databinding.api.value.ValueValidationResult
import org.hexworks.cobalt.databinding.internal.exception.CircularBindingException
import org.hexworks.cobalt.databinding.internal.extensions.asInternalProperty

@Suppress("UNCHECKED_CAST")
class ListBinding<S, T>(
    source: ObservableList<S>,
    converter: (S) -> T
) : BaseBinding<PersistentList<S>, PersistentList<T>>(
    source = source,
    target = source.value.map { converter(it) }.toProperty().asInternalProperty(),
    subscriptions = mutableListOf()
) {

    override val name = "ListBinding"

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
                val maybeNewValue: ValueValidationResult<PersistentList<T>> = target.transformValue {
                    // This is not nice...but the thing is that in order to make ObservableList only emit ListChanges
                    // in a type safe way we'd have to add it as a type parameter to the whole property abstraction
                    // ...which would also break the API
                    (type as? ListChange)?.let {
                        when (type) {
                            is ListAdd<*> -> oldValue.add(converter(type.element as S))
                            is ListAddAt<*> -> oldValue.add(type.index, converter(type.element as S))
                            is ListRemove<*> -> oldValue.remove(converter(type.element as S))
                            is ListRemoveAt -> oldValue.removeAt(type.index)
                            is ListSet<*> -> oldValue.set(type.index, converter(type.element as S))
                            is ListAddAll<*> -> oldValue.addAll(type.elements.map { converter(it as S) })
                            is ListAddAllAt<*> -> oldValue.addAll(type.index, type.c.map { converter(it as S) })
                            is ListRemoveAll<*> -> oldValue.removeAll(type.elements.map { converter(it as S) })
                            is ListRemoveAllWhen<*> -> oldValue.removeAll {
                                (type.predicate as (T) -> Boolean)(it)
                            }
                            is ListRetainAll<*> -> oldValue.retainAll(type.elements.map { converter(it as S) })
                            ListClear -> oldValue.clear()
                            is ListPropertyChange<*> -> oldValue
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
