# Cobalt

**Cobalt** is a small, dependency-light *multiplatform* utilities library for Kotlin with
modules for *data binding* (observable properties and bindings), *eventing* (an event bus)
and *logging*. It is meant to be used from Kotlin; the API is not tuned for Java or plain JS.

## Targets

`jvm` (Java 17 bytecode, also used by Android consumers), `js`, `wasmJs`, `iosX64`,
`iosArm64`, `iosSimulatorArm64`, `macosX64`, `macosArm64`, `linuxX64`, `linuxArm64`,
`mingwX64`.

The only dependency is `kotlinx-collections-immutable`.

## Usage

```kotlin
dependencies {
    implementation("org.hexworks.cobalt:cobalt.core:2.0.0")
}
```

### Data binding

```kotlin
val name = "Bob".toProperty()
val greeting = name.bindTransform { "Hello, $it" }
name.onChange { println(it.newValue) }
name.value = "Alice"          // prints Alice; greeting.value is now "Hello, Alice"
```

### Events

```kotlin
data class PlayerMoved(val x: Int, val y: Int, override val emitter: EventSource) : Event {
    override val key get() = PlayerMoved.key

    companion object : EventDescriptor<PlayerMoved> {
        override val key = "PlayerMoved"
        override val eventType = PlayerMoved::class
    }
}

val bus = EventBus.create()
bus.simpleSubscribeTo(PlayerMoved) { println("moved to ${it.x},${it.y}") }
bus.publish(PlayerMoved(1, 2, emitter = this))   // `this` implements EventSource
```

### Logging

Cobalt has no logging framework dependency. Statements at or above `LoggerFactory.level`
go to `LoggerFactory.sink`, which prints to the console unless you plug in your own:

```kotlin
LoggerFactory.level = LogLevel.DEBUG
LoggerFactory.sink = LogSink { level, name, message, throwable ->
    myFramework.log(level.name, "$name: $message", throwable)
}

private val logger = LoggerFactory.getLogger(MyClass::class)
logger.debug { "lazy message" }
```

## Build

```sh
./gradlew jvmTest jsNodeTest wasmJsNodeTest linuxX64Test   # host-runnable test suites
./gradlew publishToMavenLocal                               # unsigned local publish
```

Publishing to Maven Central runs from the `Publish` GitHub workflow on a release and signs
artifacts with the key passed as `ORG_GRADLE_PROJECT_signingInMemoryKey`.

## License

Apache 2.0, see [LICENSE](LICENSE).
