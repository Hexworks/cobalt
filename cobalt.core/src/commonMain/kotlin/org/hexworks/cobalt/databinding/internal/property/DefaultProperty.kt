package org.hexworks.cobalt.databinding.internal.property

import org.hexworks.cobalt.databinding.api.property.PropertyValidator
import org.hexworks.cobalt.databinding.internal.property.base.BaseProperty

@Suppress("UNCHECKED_CAST")
internal class DefaultProperty<T>(
    initialValue: T,
    optionalName: String?,
    validator: PropertyValidator<T> = { _, _ -> true }
) : BaseProperty<T>(
    initialValue = initialValue,
    name = optionalName ?: "DefaultProperty",
    validator = validator
) {
    companion object
}
