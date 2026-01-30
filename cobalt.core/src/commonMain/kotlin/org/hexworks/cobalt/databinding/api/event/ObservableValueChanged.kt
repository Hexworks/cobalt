package org.hexworks.cobalt.databinding.api.event

import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.events.api.Event
import org.hexworks.cobalt.events.api.EventDescriptor
import org.hexworks.cobalt.events.api.EventSource

/**
 * Fired when an [ObservableValue] **changes** its value.
 */
data class ObservableValueChanged<out T>(
    val oldValue: T,
    val newValue: T,
    val observableValue: ObservableValue<T>,
    val type: ChangeType,
    override val emitter: EventSource,
    override val trace: Iterable<Event> = listOf(),
    override val key: String = ObservableValueChanged.key
) : Event {
    companion object : EventDescriptor<ObservableValueChanged<*>> {
        override val key: String = "ObservableValueChanged"
        override val eventType = ObservableValueChanged::class
    }
}

sealed class ChangeType
object ScalarChange : ChangeType() {
    override fun toString() = "ChangeType.ScalarChange"
}

sealed class ListChange : ChangeType()
data class ListAdd<T>(val element: T) : ListChange()
data class ListAddAt<T>(val index: Int, val element: T) : ListChange()
data class ListRemove<T>(val element: T) : ListChange()
data class ListRemoveAt(val index: Int) : ListChange()
data class ListSet<T>(val index: Int, val element: T) : ListChange()
data class ListAddAll<T>(val elements: Collection<T>) : ListChange()
data class ListAddAllAt<T>(val index: Int, val c: Collection<T>) : ListChange()
data class ListRemoveAll<T>(val elements: Collection<T>) : ListChange()
data class ListRemoveAllWhen<T>(val predicate: (T) -> Boolean) : ListChange()
data class ListRetainAll<T>(val elements: Collection<T>) : ListChange()
data class ListPropertyChange<T>(val changeEvent: ObservableValueChanged<T>) : ListChange()
object ListClear : ListChange() {
    override fun toString() = "ChangeType.ListChange.ListClear"
}

sealed class SetChange : ChangeType()
data class SetAdd<T>(val element: T) : SetChange()
data class SetRemove<T>(val element: T) : SetChange()
data class SetAddAll<T>(val elements: Collection<T>) : SetChange()
data class SetRemoveAll<T>(val elements: Collection<T>) : SetChange()
data class SetRemoveAllWhen<T>(val predicate: (T) -> Boolean) : SetChange()
data class SetRetainAll<T>(val elements: Collection<T>) : SetChange()
data class SetPropertyChange<T>(val changeEvent: ObservableValueChanged<T>) : SetChange()
object SetClear : SetChange() {
    override fun toString() = "ChangeType.SetChange.SetClear"
}

sealed class MapChange : ChangeType()
data class MapPut<K : Any, V>(val key: K, val value: V) : MapChange()
data class MapPutAll<K : Any, V>(val m: Map<out K, V>) : MapChange()
data class MapRemove<K : Any>(val key: K) : MapChange()
data class MapRemoveWithValue<K : Any, V>(val key: K, val value: V) : MapChange()
data class MapPropertyChange<T>(val changeEvent: ObservableValueChanged<T>) : MapChange()
object MapClear : MapChange() {
    override fun toString() = "ChangeType.MapChange.MapClear"
}
