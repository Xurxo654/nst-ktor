package nst.api

import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import nst.api.repositories.ListSceneRepository
import nst.api.repositories.ListStudioRepository
import nst.api.repositories.SceneRepository
import nst.api.repositories.StudioRepository

suspend fun Application.configureTestApp() {
    dependencies {
        provide<StudioRepository> {
            ListStudioRepository()
        }
        provide<SceneRepository> {
            ListSceneRepository()
        }
    }
    configureSerialization()
    configureRouting()
}