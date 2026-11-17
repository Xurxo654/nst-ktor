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
            } catch (_: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
        get("/studios/{code}") {
            val code = call.parameters["code"] ?: ""
            val studio = repository.getStudio(code) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(studio)
        }
        get("/scenes") {
            val scenes = listOf(
                Scene(1,
                    "The Pledge: Part One",
                    "Serena Blair takes advantage of eager pledge, Ashley Adams",
                    10,
                    "2016-04-29",
                    Studio("Web Young", "WY")),
                Scene(2,
                    "The Pledge: Part Two",
                    "Serena needs further convincing that Ashley is up for sisterhood.",
                    10,
                    "2016-05-13",
                    Studio("Web Young", "WY"))
            )
            call.respond(scenes)
        }
        get("/scenes/{id}") {
            val scene = Scene(
                1,
                "The Pledge: Part One",
                "Serena Blair takes advantage of eager pledge, Ashley Adams",
                10,
                "2016-04-29",
                Studio("Web Young", "WY")
            )
            call.respond(scene)
        }
        staticResources("/static", "static")

    }
}