package org.hexworks.cobalt.events.api

/**
 * Used to scope [Event]s. Each [EventScope] has a separate
 * namespace from other [EventScope]s and [Event]s sent with different
 * scopes are completely isolated from each other.
 * Tip: use `object`s to implement [EventScope] for ease of use, eg:
 * ```kotlin
 * object MyEventScope : EventScope
 * ```
 */
interface EventScope {
    companion object
}
