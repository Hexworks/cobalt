package org.hexworks.cobalt.databinding.api.binding

import org.hexworks.cobalt.databinding.api.collection.ObservableList
import org.hexworks.cobalt.databinding.api.collection.ObservableListBinding
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.binding.ComputedBinding
import org.hexworks.cobalt.databinding.internal.binding.ListBinding
import org.hexworks.cobalt.databinding.internal.collections.ListBindingDecorator

/**
 * Creates a [Binding] which will contain the transformed value of this [ObservableValue]
 * from its type [S] to a new type [T].
 */
fun <S : Any, T : Any> ObservableValue<S>.bindTransform(transformer: (S) -> T): Binding<T> {
    return ComputedBinding(this, transformer)
}

/**
 * Creates a [Binding] which will contain the mapped values of this [ObservableList]
 * from its type [S] to a new type [T].
 */
fun <S : Any, T : Any> ObservableList<S>.bindMap(transformer: (S) -> T): ObservableListBinding<T> {
    return ListBindingDecorator(ListBinding(this, transformer))
}

/**
 * Creates a [Binding] which will contain the flattened (flatMap) values of this [ObservableList]
 */
//fun <T : Any, R: Any> ListProperty<T>.bindFlatMap(
//    transformer: (ObservableValue<T>) -> ObservableList<R>
//): ObservableListBinding<R> {
//    return ListBindingDecorator(ListBinding(this, transformer))
//}









