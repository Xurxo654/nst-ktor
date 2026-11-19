package nst.api

import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies

fun Application.configureDependencyInjection() {
    dependencies {
        provide<StudioRepository> {
            ListStudioRepository()
        }
        provide<SceneRepository> {
            ListSceneRepository()
        }
    }
}