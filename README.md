Maestro is a sample project that demonstrates an architecture pattern that I'm currently calling "ViewModelOwner".

## The Use Case

Maestro [theoretically] demonstrates a pattern for an app with multiple distinct workflows (sequences of screens). These workflows each have two critical requirements.
- Some form of **_shared state_**, persisting only for as long as the workflow's **_nav graph_** does.
- One or more **_navigation destinations_** that can be used by **_all_** of the workflows.
  - The state of this destination must also be controlled (and therefore owned) by the top-level workflow.

In Maestro, the workflows are represented by instruments. When you select an instrument (top-level workflow & shared state), you must also select a piece of music (reusable navigation destination).

## Getting Started

The implementation starts with a typical Compose/Hilt `ViewModel` pattern as a base:
```
// Guitar
@HiltViewModel
class GuitarViewModel @Inject constructor(): ViewModel() {
    // business logic...
}

// Piano
@HiltViewModel
class PianoViewModel @Inject constructor(): ViewModel() {
    // business logic...
}

// SheetMusic
@HiltViewModel
class SheetMusicViewModel @Inject constructor(): ViewModel() {
    // business logic...
}

// NavHost
navigation<GuitarSectionRoute> {
    composable<GuitarRoute> {
        // content...
    }
    composable<SheetMusicRoute> {
        // content...
    }
}
navigation<PianoSectionRoute> {
    composable<PianoRoute> {
        // content...
    }
    composable<SheetMusicRoute> {
        // content...
    }
}
```

This structure immediately begs a question: if `PianoRoute` and `SheetMusicRoute` need to share _complex_ state, how do we avoid having a single `ViewModel` for the entire "piano" workflow?

## The "Shared View Model"

First, we need a place to store that shared state. It must be lifecycle aware and should only persist as long as `PianoSectionRoute` is in the navigation hierarchy. We can use a `ViewModel` for this. However, we cannot simply create this `ViewModel` in the standard fashion:
```
// this will not work
val viewModel = hiltViewModel()
```

The `ViewModel` instance returned from `hiltViewModel()` will be scoped to the nearest `composable` destination of the current nav graph. This is not what we need.

We can create an extension which scopes the returned instance to a `navigation` (graph) instead of a `composable` (destination).
```
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

// usage

navigation<PianoSectionRoute> {
    composable<PianoRoute> { entry ->
        val sharedViewModel = entry.sharedViewModel(navController)
    }
    composable<SheetMusicRoute> { entry ->

        // this is the SAME instance as that one ^^
        val sharedViewModel = entry.sharedViewModel(navController)
    }
}
```

Now you're at a crossroads. If you're fine with either of the following, you can stop here.
- Use a _single_ `ViewModel` that contains the state/logic for the entire workflow.
- Use _two_ `ViewModel`s (and therefore, two sources of state), one for shared state, one for local state.

There is a third option, but it requires some more architecting.
- Use a _single_ `ViewModel`, which contains local state/logic and provides, but **_does not contain_**, shared state.

## Assisted Injection (Dagger/Hilt)

To do this, we use an assisted injection to inject the graph-level "shared" `ViewModel` into the destination-level `ViewModel`.

> [!NOTE]
> In a typical use case, `ViewModel`s _should not_ depend on one another. This recommendation is largely due to how the system handles the lifecycle of a `ViewModel`. The lifecycle of any `ViewModel` is tied directly to its `LifecycleOwner`. If the `LifecycleOwner` is destroyed, so is the `ViewModel`. In this pattern however, the "shared" `ViewModel` is scoped to the outer _navigation graph_. That means it will _always_ survive at least as long as any dependent `ViewModel`s.

As an example of assisted injection, here is the `PianoHomeViewModel`:
```
@HiltViewModel(assistedFactory = PianoHomeViewModel.Factory::class)
class PianoHomeViewModel @AssistedInject constructor(
    @Assisted private val owner: PianoViewModel
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(owner: PianoViewModel): PianoHomeViewModel
    }
}
```

This instructs Hilt that this dependency will be injected using a factory callback at runtime. We now need to implement that callback in the `NavHost`:
```
navigation<PianoSectionRoute> {
    composable<PianoHomeRoute> { entry ->
        val sectionViewModel: PianoViewModel = entry.sharedViewModel(navController)
        val destinationViewModel = hiltViewModel<PianoHomeViewModel, PianoHomeViewModel.Factory> { factory ->
    
            // manually provide the `@Assisted` dependency
            factory.create(sectionViewModel)
        }
    }
}
```
> [!CAUTION]
> The "owner" or "parent" `ViewModel` must **_always_** be scoped to the same `navigation` as their dependent `ViewModel`s. 

Now you can add your workflow's shared state/business logic in one place, and each screen's local state/business logic in another!

> [!TIP]
> See [this article](https://proandroiddev.com/hilt-viewmodels-assisted-injection-aca2d6ee581d) by Fred Porciúncula for a more in-depth explanation of assisted injection.

## Going Further

Now, imagine you have a navigation hierarchy similar to Maestro.

![Screenshot 2025-05-28 at 8 30 10 PM](https://github.com/user-attachments/assets/246ee78a-e675-44ca-96b7-592e76863d09)

Note that the Guitar and Piano workflows both need to use the `SheetMusicScreen`. To support this shared dependency, we first need an interface. This should define what state/logic needs to be provided to the `SheetMusicViewModel` by its `ViewModel` "owner".
```
interface SheetMusicOwner {
    val beatsPerMin: Int
    val instrument: String
}
```

Then, we implement this interface in the `PianoViewModel`, which is one of the `SheetMusicViewModel`'s owners.
```
@HiltViewModel
class PianoViewModel @Inject constructor(): SheetMusicOwner, ViewModel() {

    override val instrument: String
        get() = "Piano"

    override val beatsPerMin: Int
        get() = 100
}
```

Then, we again use assisted injection to inject the dependency. However in this instance, we will [delegate](https://kotlinlang.org/docs/delegation.html) the members of `SheetMusicOwner` to the injected instance. That way, we avoid boilerplate we would otherwise need to expose the state/logic provided by `owner`.
```
@HiltViewModel(assistedFactory = SheetMusicViewModel.Factory::class)
class SheetMusicViewModel @AssistedInject constructor(
    @Assisted private val owner: SheetMusicOwner,
): SheetMusicOwner by owner, ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(owner: SheetMusicOwner): SheetMusicViewModel
    }
}
```
