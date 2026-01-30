package org.hexworks.cobalt.events

import org.hexworks.cobalt.core.api.UUID
import org.hexworks.cobalt.core.api.behavior.DisposedByException
import org.hexworks.cobalt.events.api.*
import org.hexworks.cobalt.events.internal.ApplicationScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class EventBusTest : EventSource {

    override val id: UUID = UUID.randomUUID()

    private val target = EventBus.create()

    @Test
    fun When_a_subscription_for_a_scope_and_key_is_cancelled_other_subscriptions_for_the_same_combination_shouldnt_be_cancelled() {

        val subscription0 = target.simpleSubscribeTo(TestEvent) {}
        val subscription1 = target.simpleSubscribeTo(TestEvent) {
        }

        subscription0.dispose()

        assertFalse(subscription1.disposed)
    }

    @Test
    fun When_a_subscription_for_a_scope_and_key_is_cancelled_other_subscriptions_for_the_same_combination_should_be_notified_when_an_event_is_published() {

        var sub0Notified = false
        var sub1Notified = false


        val subscription0 = target.simpleSubscribeTo(TestEvent) {
            sub0Notified = true
        }
        target.simpleSubscribeTo(TestEvent) {
            sub1Notified = true
        }

        subscription0.dispose()
        target.publish(TestEvent(this))

        assertFalse(sub0Notified)
        assertTrue(sub1Notified)

    }

    @Test
    fun When_subscribed_to_an_event_and_the_proper_event_is_published_then_the_subscriber_should_be_notified() {

        var notified = false

        target.simpleSubscribeTo(TestEvent) {
            notified = true
        }

        target.publish(TestEvent(this))

        assertEquals(true, notified, "Subscriber was not notified.")
    }

    @Test
    fun When_subscribed_to_an_event_and_scope_and_the_proper_event_is_published_then_the_subscriber_should_be_notified() {

        var notified = false
        target.simpleSubscribeTo(TestEvent, TestScope) {
            notified = true
        }

        target.publish(TestEvent(this), TestScope)

        assertEquals(true, notified, "Subscriber was not notified.")
    }

    @Test
    fun When_subscribed_to_an_event_and_scope_and_the_proper_event_is_published_but_with_wrong_scope_then_the_subscriber_should_not_be_notified() {

        var notified = false
        target.simpleSubscribeTo(TestEvent) {
            notified = true
        }

        target.publish(TestEvent(this), TestScope)

        assertEquals(false, notified, "Subscriber should not have been notified.")
    }

    @Test
    fun When_subscribed_to_an_event_with_a_key_and_the_proper_event_is_published_then_the_subscriber_should_be_notified() {

        var notified = false

        target.subscribeTo(TestEvent) {
            notified = true
            KeepSubscription
        }

        target.publish(TestEvent(this))

        assertEquals(true, notified, "Subscriber was not notified.")
    }


    @Test
    fun When_EventBus_has_multiple_subscribers_for_the_same_event_all_should_be_notified_when_that_event_is_fired() {

        val notifications = mutableListOf<Int>()

        target.simpleSubscribeTo(TestEvent) {
            notifications += 0
        }
        target.simpleSubscribeTo(TestEvent) {
            notifications += 1
        }

        target.publish(TestEvent(this))

        assertEquals(listOf(0, 1), notifications, "All subscribers should have been notified.")
    }

    @Test
    fun When_EventBus_has_multiple_subscribers_for_the_same_event_but_different_scopes_only_one_should_be_notified_when_that_event_is_fired() {

        val notifications = mutableListOf<EventScope>()
        target.simpleSubscribeTo(TestEvent, TestScope) {
            notifications.add(TestScope)
        }
        target.simpleSubscribeTo(TestEvent, ApplicationScope) {
            notifications.add(ApplicationScope)
        }

        target.publish(TestEvent(this), TestScope)

        assertEquals(
            mutableListOf<EventScope>(TestScope),
            notifications,
            "Only the subscriber with TestScope should have been notified."
        )
    }

    @Test
    fun When_subscribed_to_an_event_Then_subscriber_should_be_present_in_EventBus() {

        val subscription = target.simpleSubscribeTo(TestEvent) { }

        assertEquals(
            expected = listOf(subscription),
            actual = target.fetchSubscribersOf(TestEvent, ApplicationScope).toList(),
            message = "Subscribers should be empty."
        )

    }

    @Test
    fun When_unsubscribed_from_event_Then_subscriber_should_not_be_present_in_EventBus() {

        target.simpleSubscribeTo(TestEvent) { }.dispose()

        assertEquals(
            expected = listOf(),
            actual = target.fetchSubscribersOf(TestEvent, ApplicationScope).toList(),
            message = "Subscribers should be empty."
        )

    }

    // TODO: cancelScope!
    @Test
    fun When_invoking_callback_causes_exception_subscriber_should_be_cancelled_with_exception() {
        val exception = IllegalArgumentException()
        val expectedState = DisposedByException(exception)
        val subscription = target.simpleSubscribeTo(TestEvent) {
            throw exception
        }

        target.publish(TestEvent(this))

        assertEquals(
            expected = expectedState,
            actual = subscription.disposeState,
            message = "Subscriber should have been cancelled with exception"
        )
    }

    data class TestEvent(
        override val emitter: EventSource,
        override val key: String = TestEvent.key,
        override val trace: Iterable<Event> = listOf(),
    ) : Event {

        companion object : EventDescriptor<TestEvent> {
            override val key: String = "TestEvent"
            override val eventType = TestEvent::class
        }
    }

    object TestScope : EventScope
}
