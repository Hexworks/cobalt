package org.hexworks.cobalt.databinding.internal.property

import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.databinding.api.property.PropertyDelegate
import kotlin.reflect.KProperty

internal class DefaultPropertyDelegate<T>(private val property: Property<T>) : PropertyDelegate<T>,
    Property<T> by property {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return this.property.value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.property.value = value
    }

    companion object
}
