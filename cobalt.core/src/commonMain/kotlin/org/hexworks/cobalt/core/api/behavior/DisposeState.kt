package org.hexworks.cobalt.core.api.behavior

/**
 * Contains information about whether a [Disposable] object is disposed or not.
 */
sealed class DisposeState(val isDisposed: Boolean) {
    companion object
}

/**
 * The [Disposable] is not disposed.
 */
object NotDisposed : DisposeState(false)

/**
 * The [Disposable] was disposed by the user.
 */
object DisposedManually : DisposeState(true)

/**
 * The [Disposable] was disposed by some event.
 */
data class DisposedByEvent<T : Any>(val event: T) : DisposeState(true)

/**
 * The [Disposable] was disposed due to an [Exception].
 */
data class DisposedByException<E: Exception>(val exception: E) : DisposeState(true)
