package org.hexworks.cobalt.databinding.internal.binding

import org.hexworks.cobalt.databinding.api.binding.bindContainsAllWith
import org.hexworks.cobalt.databinding.api.binding.bindContainsWith
import org.hexworks.cobalt.databinding.api.binding.bindFlatten
import org.hexworks.cobalt.databinding.api.binding.bindIndexOfWith
import org.hexworks.cobalt.databinding.api.binding.bindIsEmpty
import org.hexworks.cobalt.databinding.api.binding.bindIsEqualToWith
import org.hexworks.cobalt.databinding.api.binding.bindLastIndexOfWith
import org.hexworks.cobalt.databinding.api.binding.bindMinusWith
import org.hexworks.cobalt.databinding.api.binding.bindPlusWith
import org.hexworks.cobalt.databinding.api.binding.bindSize
import org.hexworks.cobalt.databinding.api.extension.ObservablePersistentCollection
import org.hexworks.cobalt.databinding.api.extension.toProperty
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class CollectionBindingsTest {

    private val prop1To3 = NUMBERS_1_TO_3.toProperty()
    private val prop4To6 = NUMBERS_4_TO_6.toProperty()
    private val prop7To9 = NUMBERS_7_TO_9.toProperty()

    @Test
    fun When_list_properties_are_flattened_Then_the_binding_value_is_properly_calculated() {
        val a = mutableListOf(1, 2, 3).toProperty()
        val b = mutableListOf(4, 5, 6).toProperty()

        val fm = mutableListOf<ObservablePersistentCollection<Int>>().toProperty()

        val result = fm.bindFlatten()

        fm.add(a)
        fm.add(b)

        assertEquals(
            expected = a.value + b.value,
            actual = result.value
        )
    }

    @Test
    fun When_list_properties_are_flattened_Then_the_binding_value_is_properly_calculated_when_items_are_removed() {
        val a = mutableListOf(1, 2, 3).toProperty()
        val b = mutableListOf(4, 5, 6).toProperty()

        val fm = mutableListOf<ObservablePersistentCollection<Int>>().toProperty()

        val result = fm.bindFlatten()

        fm.add(a)
        fm.add(b)

        fm.remove(a)

        assertEquals(
            expected = b.value,
            actual = result.value
        )
    }

    @Test
    fun When_list_properties_are_plussed_Then_the_binding_value_is_the_plussed_list() {

        val binding = prop1To3 bindPlusWith prop4To6

        assertEquals(
            expected = NUMBERS_1_TO_3 + NUMBERS_4_TO_6,
            actual = binding.value.toList()
        )
    }

    @Test
    fun When_list_property_and_binding_are_plussed_Then_the_binding_value_is_the_plussed_list() {

        val binding = prop1To3 bindPlusWith prop4To6
        val derivedBinding = binding bindPlusWith prop7To9

        assertEquals(
            expected = NUMBERS_1_TO_3 + NUMBERS_4_TO_6 + NUMBERS_7_TO_9,
            actual = derivedBinding.value.toList()
        )
    }

    @Test
    fun When_list_properties_are_minused_Then_the_binding_value_is_the_minused_list() {

        val binding1To6 = prop1To3 bindPlusWith prop4To6

        val subtracted = binding1To6 bindMinusWith prop4To6

        assertEquals(NUMBERS_1_TO_3, subtracted.value)
    }

    @Test
    fun When_list_property_and_binding_are_minused_Then_the_binding_value_is_the_minused_list() {

        val binding = prop1To3 bindPlusWith prop4To6
        val derivedBinding = binding bindPlusWith prop7To9

        val subtracted = derivedBinding bindMinusWith prop7To9

        assertEquals(NUMBERS_1_TO_3 + NUMBERS_4_TO_6, subtracted.value.toList())
    }

    @Test
    fun When_properties_are_equal_Then_derived_is_equal_binding_is_true() {

        val binding = prop1To3 bindIsEqualToWith prop1To3

        assertEquals(true, binding.value)
    }

    @Test
    fun When_properties_are_not_equal_Then_derived_is_equal_binding_is_false() {

        val binding = prop1To3 bindIsEqualToWith prop4To6

        assertEquals(false, binding.value)
    }

    @Test
    fun When_list_size_is_bound_Then_the_binding_value_is_correct() {

        val binding = prop1To3.bindSize()

        assertEquals(3, binding.value)
    }

    @Test
    fun When_list_size_is_bound_Then_adding_to_the_list_changes_the_binding_value() {

        val binding = prop1To3.bindSize()

        prop1To3.add(4)

        assertEquals(4, binding.value)
    }

    @Test
    fun When_list_emptiness_is_bound_Then_the_binding_value_is_correct() {

        val binding = prop1To3.bindIsEmpty()

        assertEquals(false, binding.value)
    }

    @Test
    fun When_list_emptiness_is_bound_and_the_list_becomes_empty_Then_the_binding_value_changes() {

        val binding = prop1To3.bindIsEmpty()

        prop1To3.clear()

        assertEquals(true, binding.value)
    }

    @Test
    fun When_list_contains_is_bound_Then_the_binding_value_is_correct() {

        val prop1 = 1.toProperty()
        val binding = prop1To3.bindContainsWith(prop1)

        assertEquals(true, binding.value)
    }

    @Test
    fun When_list_contains_is_bound_and_the_number_changes_Then_the_binding_value_changes() {

        val prop1 = 1.toProperty()
        val binding = prop1To3.bindContainsWith(prop1)

        prop1.value = 4

        assertEquals(false, binding.value)
    }

    @Test
    fun When_list_contains_all_is_bound_Then_the_binding_value_is_correct() {

        val listProp = listOf(1, 2, 3).toProperty()
        val binding = prop1To3.bindContainsAllWith(listProp)

        assertEquals(true, binding.value)
    }

    @Test
    fun When_list_contains_all_is_bound_and_the_list_changes_Then_the_binding_value_changes() {

        val listProp = listOf(1, 2, 3).toProperty()
        val binding = prop1To3.bindContainsAllWith(listProp)

        listProp.add(4)

        assertEquals(false, binding.value)
    }

    @Test
    fun When_list_index_of_is_bound_Then_the_binding_value_is_correct() {

        val numProp = 1.toProperty()
        val binding = prop1To3.bindIndexOfWith(numProp)

        assertEquals(0, binding.value)
    }

    @Test
    fun When_list_index_of_is_bound_and_the_number_changes_Then_the_binding_value_changes() {

        val numProp = 1.toProperty()
        val binding = prop1To3.bindIndexOfWith(numProp)

        numProp.value = 2

        assertEquals(1, binding.value)
    }

    @Test
    fun When_list_last_index_of_is_bound_Then_the_binding_value_is_correct() {

        val numProp = 1.toProperty()
        val binding = prop1To3.bindLastIndexOfWith(numProp)

        assertEquals(0, binding.value)
    }

    @Test
    fun When_list_last_index_of_is_bound_and_the_number_changes_Then_the_binding_value_changes() {

        val numProp = 1.toProperty()
        val binding = prop1To3.bindLastIndexOfWith(numProp)

        prop1To3.add(1)

        assertEquals(3, binding.value)
    }

    companion object {

        val NUMBERS_1_TO_3: Collection<Int> = listOf(1, 2, 3)
        val NUMBERS_4_TO_6: Collection<Int> = listOf(4, 5, 6)
        val NUMBERS_7_TO_9: Collection<Int> = listOf(7, 8, 9)
    }
}
