package org.hexworks.cobalt.events.api

/**
 * The possible result of a callback.
 * By returning one of these objects you signal what should
 * happen with your callback (more below).
 */
sealed class CallbackResult {
    companion object
}

/**
 * Signals that the subscription should be kept.
 */
object KeepSubscription : CallbackResult()

/**
 * Signals that the subscription should be disposed.
 */
object DisposeSubscription : CallbackResult()
