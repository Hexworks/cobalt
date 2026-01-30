package org.hexworks.cobalt.databinding.internal.property

import org.hexworks.cobalt.databinding.api.extension.toProperty
import org.hexworks.cobalt.databinding.api.property.Property
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class DefaultPropertyDelegateTest {

    lateinit var property: Property<String>

    @BeforeTest
    fun Set_up() {
        property = XUL.toProperty()
    }

    @Test
    fun When_property_is_delegated_to_a_variable_the_variable_should_have_the_same_value_as_the_property() {
        val target: String by property.asDelegate()

        assertEquals(expected = XUL, actual = target)
    }

    @Test
    fun When_a_property_is_updated_its_delegate_should_be_updated_as_well() {
        val target: String by property.asDelegate()

        property.value = QUX

        assertEquals(expected = QUX, actual = target)
    }

    companion object {
        const val XUL = "XUL"
        const val QUX = "QUX"
    }
}
