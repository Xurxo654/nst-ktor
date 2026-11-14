package nst.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import kotlinx.serialization.SerializationException

suspend fun Application.configureRouting() {
    val repository = dependencies.resolve<StudioRepository>()

    routing {
        get("/studios") {
            val studios = repository.allStudios()
            call.respond(studios)
        }
        post("/studios") {
            try {
                val studio = call.receive<Studio>()
                repository.addStudio(studio)
                call.respond(HttpStatusCode.Created)
            } catch (_: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (_: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
        staticResources("/static", "static")

    }
}