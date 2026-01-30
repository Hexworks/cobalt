package org.hexworks.cobalt.core.api.extensions

typealias Predicate<T> = Function1<T, Boolean>

infix fun <T> Predicate<T>.and(other: Predicate<T>): Predicate<T> = { value ->
    this(value) && other(value)
}

fun <T> Predicate<T>.not(): Predicate<T> = { value ->
    !this(value)
}

infix fun <T> Predicate<T>.or(other: Predicate<T>): Predicate<T> = { value ->
    this(value) || other(value)
}
