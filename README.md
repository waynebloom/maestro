Maestro is a sample project that demonstrates an architecture pattern that I'm currently calling "ViewModelOwner".

## Use Case

Imagine an app with several distinct nav graphs (or workflows).
- In Maestro, these are the different instrument graphs.
- Each one has a "home" screen and a "sheet music" screen.

Each of these workflows has a bit of shared state that each screen will modify in some way.
- In GuitarHome, you can change the capo and the pitch of the strings.
- In SheetMusic, you can change the key and the tempo (bpm)

Each workflow, despite being distinct from one another, reuses one particular screen.
- Each instrument workflow needs a sheet music screen

## ViewModelOwner Pattern

This pattern combines two implementations
- The "Shared View Model"
- Hilt runtime dependency injection via `@AssistedInject` and `@AssistedFactory`
