package nst.api.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.SerializationException
import nst.api.models.Studio
import nst.api.repositories.StudioRepository

fun Route.studioRoutes(repository: StudioRepository) {

    route("/studios") {
        get {
            val studios = repository.allStudios()
            call.respond(studios)
        }
        get("/{id}") {
            val code = call.parameters["id"]?: ""
            val studio = repository.getStudio(code) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(studio)
        }
        post {
            try {
                val studio = call.receive<Studio>()
                repository.addStudio(studio)
                call.respond(HttpStatusCode.Created)
            } catch (_: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (_: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}