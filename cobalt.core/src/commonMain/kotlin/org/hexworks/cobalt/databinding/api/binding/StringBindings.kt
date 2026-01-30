package org.hexworks.cobalt.databinding.api.binding

import org.hexworks.cobalt.databinding.api.converter.toConverter
import org.hexworks.cobalt.databinding.api.extension.toInternalProperty
import org.hexworks.cobalt.databinding.api.value.ObservableValue
import org.hexworks.cobalt.databinding.internal.binding.ComputedBinding
import org.hexworks.cobalt.databinding.internal.binding.ComputedDualBinding
import org.hexworks.cobalt.databinding.internal.binding.UnidirectionalBinding

fun ObservableValue<String>.bindIsEmpty(): Binding<Boolean> {
    val converter = { str: String -> str.isEmpty() }.toConverter()
    return UnidirectionalBinding(
        source = this,
        target = converter.convert(this.value).toInternalProperty(),
        converter = converter
    )
}

fun ObservableValue<String>.bindIsBlank(): Binding<Boolean> {
    val converter = { str: String -> str.isBlank() }.toConverter()
    return UnidirectionalBinding(
        source = this,
        target = converter.convert(this.value).toInternalProperty(),
        converter = converter
    )
}

infix fun ObservableValue<String>.bindPlusWith(other: ObservableValue<String>): Binding<String> {
    return ComputedDualBinding(this, other) { thisValue, otherValue -> thisValue + otherValue }
}

infix fun ObservableValue<String>.bindEqualsWith(other: ObservableValue<String>): Binding<Boolean> {
    return ComputedDualBinding(this, other) { thisValue, otherValue -> thisValue == otherValue }
}

infix fun ObservableValue<String>.bindEqualsIgnoreCase(other: ObservableValue<String>): Binding<Boolean> {
    return ComputedDualBinding(
        this,
        other
    ) { thisValue, otherValue -> thisValue.equals(otherValue, ignoreCase = true) }
}

fun ObservableValue<String>.length(): Binding<Int> {
    return ComputedBinding(this) { it.length }
}
