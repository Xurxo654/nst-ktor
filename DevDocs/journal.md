# 26-11-12
## Introduction
This is my attempt to make the naughty stuff and things api utilizing kotlin and ktor. Although I still need to make a lot of decisions regarding the tagging system, I wanted to get the project started as I learn more about ktor. Over the course of working on this, I expect a lot to change and that is alright. One of my goals in this project is to maintain a requirements document to keep track of what I need the api to do. These requirements will start from the endpoints and work their way through implementation. I will use placeholders and static data while developing the dynamic way data will be processed by the system. This will hopefully lead to a very testable system and ultimately a well defined db schema before I make a database.
## Progress
- Started requirements for Studio
- Started Implementation and testing for studio requirements 
# 26-11-14
## Progress
- Added a post endpoint to studios
  - to support this I created a `StudioRepository`interface and a list based implementation for it
  - I also added the dependency injection plugin to support injection the `StudioRepository` into `Routing.kt`
- Created the requirement "post endpoint should not allow creation of duplicate studio codes"
- Added test for requirement "post endpoint should not allow creation of duplicate studio codes"
- Refactored studios POST endpoint to prevent creation of duplicate studio codes
  - refactored ListStudioRepository to throw an `IllegalArgumentException` when there is an existing studio with the code of the new studio
  - refactored `Routing.kt` to catch an `IllegalArgumentException`
- Added test for requirement "Application needs a get endpoint to retrieve a single studio"
- Implemented code for "Application needs a get endpoint to retrieve a single studio"
  - refactored Studio repository to add `getByCode()`
  - added `studios\{code}` endpoint
# 26-11-15
## Research
While writing the requirements for `Scene` I was thinking about how to handle imprecise dates for when I only know the year or month and year that a scene was released. I found that a way to handle this is to store the release date but also have a second field that tracks the precision (year, month, day). When the day is not know the date uses the first of the month in the date field and then records that month is the most precise part of the date. When a month is not know, the date defaults to january 1 of the year and year is recorded as the most precise date known.
## Progress
- Started writing basic requirements for `Scene`
- Created `Scene` class
- Created get single scene endpoint
- Added requirement that `Scene` needs unique ID
- Implemented unique ID on `Scene`
- refactored get scene by id test
- refactored routing to return updated scene
# 26-11-16
## Progress
- Added and implemented requirement: Application needs a get endpoint to retrieve all scenes
- Added requirements for creating a scene
- created tests for creating a scene
# 26-11-18
## Research
While trying to make a simple list based implementation of my `SceneRepository` I discovered a challenge with mocking data. In practice, the `SceneRepository` won't store the `Studio` but store the `studioCode` as a foreign key. In order to simulate this I had to create a `SceneDAO` class and convert that to a `Scene` by using both the `SceneRepository` and `StudioRepository`
## Progress
- Created `SceneRepository` Interface
- Implemented `SceneRepository` on `ListSceneRepository`
- Refactored tests to accommodate `SceneRepository`
- Created test for POST scenes with optional fields creates scene
- Implemented scenes POST method
# 26-11-25
## Research
While performing the refactor of `Routing` I first tried to resolve the repositories in the new `xxRoutes` files but that would have required making the functions suspend. 
I found a simple solution that involved using a property delegate.
But ultimately decided to stick with resolving the DI in my `Routing` file and passing them to the `xxRoutes`.
I chose this approach because it kept my DI in a single place and I was worried that the property delegate approach might need more set up when testing the code.
```kotlin
// example of property delegate
fun Route.studioRoutes() {
    val repository: StudioRepository by application.dependicies
    
    route("/studios") {
        get { call.respond(repository.allStudios())}
    }
}
```
PATCH is the appropriate endpoint for partially updating an endpoint.
Decided the current approach to updating will be to utilize an update request with nullable properties. I'll treat null as not being updated and empty strings as a trigger to update the field to null.
## Progress
- Implemented no duplicate titles per studio requirement
- Refactored Routing to domain specific files
- Added Scene update requirement
- Refactored Tests to split scene and studio tests into separate files
- Wrote tests for updating fields with values
- Implemented method for updating properties of scene
# 26-11-26
## Progress
- Added requirement to be able to delete scenes
- Created test for delete
- Implemented delete method on scenes