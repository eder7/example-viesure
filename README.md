# Viesure Assignment

Hello Viesure, here's my assignment, I hope you like it!

I did this amidst intense moving preparations so I had to hurry a lot. Please excuse me for being quite focused on showing what I consider most important.

If you want to run a test, have a look at `ArticlesControllerTest` (line coverage: 94%).

### Approach

* Show my interpretation of Clean Architecture in a minimal setting but enough to show basic principles.
* Show principles, not apply them everywhere (e.g. test on `ArticlesController` and `@Preview` annotation on `ArticleListItem` only).

### Module structure

Because there's not more than one feature or domain ("articles"), I only split the structure along boundaries typical for the Clean Architecture approach.

* `app`: entry point and view-related models for the application (depends on the modules below)
* `adapters`: adapters for the android platform, database and backend (depends on the modules below)
* `usecases`: logic and data structures and interfaces for business-relevant operations (depends on the modules below)
* `entities`: core data structure(s) and logic, potentially shared between domains (depends on the modules below)

#### Example target structure

* `app`: entry point and DI wiring only
* `adapters`: non feature-specific adapters
  * `android`: implementations of android-specific platform functionality (e.g. for accessing `CoroutineDispatcher`s and strings resources)
* `articles`: container for article-specific data and logic
  * `adapters`
    * `ui`: view-related models for the articles feature/domain (formerly directly in the `:app` module)
    * `db`: database adapter for the articles feature/domain
    * `backend`: backend adapter for the articles feature/domain
  * `usecases`: feature/domain-specific use cases
  * `entities`: feature/domain-specific entities
* `usecases`: platform and feature/domain-independent use cases
  * `platform`: interfaces for accessing platform-dependent resources (e.g. `CoroutineDispatcher`s and strings)
* `entities`: mainly data and logic that is shared among domains or are feature/domain-independent

Dependencies between modules would be lined out similar to the existing example. But `:articles:adapters:...` would depend on the global `:adapters`; the same for use cases and entities. But dependencies between domains/features would be forbidden.

In the already-existing example some platform features, e.g. for accessing `CoroutineDispatcher`s and strings resources, are already abstracted in a platform-independent way. These would then be moved into the `:usecases:platform` module, while the implementations would go into the `:adapters:android` module. This would happen in a similar fashion for the other modules as well.

`usecases` and `entities` can become pure Kotlin modules and potentially be shared among platforms. In extreme cases even view models can be made Kotlin-only.

If there is only one database and it can't be reasonably split, the respective adapter `:articles:adapters:db` can be integrated into a more wide `:adapters:db`. But there would have to be an architectural discussion on whether domain boundaries should really be that blurry.

### Comments

* For naming use cases I took the liberty to just name them `SomeVerb` instead of `SomeVerbUseCase`. I think it helps the readability, and also the distinction to other types of Interfaces is clear enough.
* I used old time API instead of desugaring to use new `java.time` API as I think a min SDK raise could potentially be around the corner.
* I'd like to use invoke operators in all use case interfaces but I can't do that yet. The reason is that it leads to a conflict when a class implements multiple of these interfaces with an invoke operator of the same signature. Let’s see whether a feature supporting this will be implemented in Kotlin. It would improve simplicity on the caller side of the use cases a lot.
* The most pragmatic solution for introducing an encrypted database was to use [SQLCipher](https://www.zetetic.net/sqlcipher/sqlcipher-for-android/), essentially a SQLite fork. Its inclusion can be problematic: If the maintainer stops support or struggles to keep up-to-date with SQLite we might end up with compatibility issues and needing to maintain our own fork and possibly a lot of migration work. More research for alternatives is needed.
* I first used an event-based approach for navigation and then converted to event-based via view model. It increased complexity a lot, but I think the implementation can still be heavily improved in terms of simplicity of use (time was limited).

### ToDo

This is what I think should happen in order to get this example more towards production quality.

* Extract dimensions into resources and reuse them
* Come up with better solution for image loading as the user experience is not optimal
  * Improve loading by caching
  * Reduce annoying flickering by using fixed image size or animation during state transitions (placeholder, error and real image)
* Implement predictive back gesture support
* Test app on multiple API levels (tested with 34)
* Introduce paging to support bigger datasets
* Convert `entities` and `usecases` into kotlin modules
* The introduction of architectural fitness functions could be considered in order to enforce the correct direction of dependencies; I don't know the necessary tooling for Kotlin modules, but if the JVM target is used, [JDepend](https://github.com/clarkware/jdepend) can be considered
