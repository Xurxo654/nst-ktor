package nst.api

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.di.dependencies
import nst.api.routes.sceneRoutes
import nst.api.routes.studioRoutes

suspend fun Application.configureRouting() {
    val sceneRepository = dependencies.resolve<SceneRepository>()
    val studioRepository = dependencies.resolve<StudioRepository>()

    routing {
        studioRoutes(studioRepository)
        sceneRoutes(sceneRepository, studioRepository)
        staticResources("/static", "static")
    }
}