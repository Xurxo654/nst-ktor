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
    val sceneRepository = dependencies.resolve<SceneRepository>()

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
            val sceneDaos = sceneRepository.getScenes()
            val scenes = sceneDaos.map { it ->
                val studio = repository.getStudio(it.studioCode) ?: return@get call.respond(HttpStatusCode.InternalServerError)
                Scene(it.id, it.title, it.description, it.rating, it.releaseDate, studio)
            }

            call.respond(scenes)
        }
        get("/scenes/{id}") {
            val id = call.parameters["id"]?.toLong() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val dao = sceneRepository.getSceneById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            val studio = repository.getStudio(dao.studioCode) ?: return@get call.respond(HttpStatusCode.InternalServerError)
            val scene = Scene(
                dao.id,
                dao.title,
                dao.description,
                dao.rating,
                dao.releaseDate,
                studio
            )
            call.respond(scene)
        }
        post("scenes") {
            try {
                val request = call.receive<SceneCreationRequest>()
                sceneRepository.createScene(request)
                call.respond(HttpStatusCode.Created)
            } catch (_: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        staticResources("/static", "static")

    }
}