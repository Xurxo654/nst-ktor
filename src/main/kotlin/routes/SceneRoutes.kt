package nst.api.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.SerializationException
import nst.api.Scene
import nst.api.SceneCreationRequest
import nst.api.SceneRepository
import nst.api.SceneUpdateRequest
import nst.api.StudioRepository
import nst.api.applyChanges

fun Route.sceneRoutes(repository: SceneRepository, studioRepository: StudioRepository) {

    route("/scenes") {
        get {
            val sceneDaos = repository.getScenes()
            val scenes = sceneDaos.map {
                val studio = studioRepository.getStudio(it.studioCode) ?: return@get call.respond(HttpStatusCode.InternalServerError)
                Scene(it.id, it.title, it.description, it.rating, it.releaseDate, studio)
            }
            call.respond(scenes)
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val dao = repository.getSceneById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            val studio = studioRepository.getStudio(dao.studioCode) ?: return@get call.respond(HttpStatusCode.InternalServerError)
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
        post {
            try {
                val request = call.receive<SceneCreationRequest>()
                repository.createScene(request)
                call.respond(HttpStatusCode.Created)
            } catch (_: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (_: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        patch("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: return@patch call.respond(HttpStatusCode.BadRequest)
            val dao = repository.getSceneById(id) ?: return@patch call.respond(HttpStatusCode.NotFound)
            val request = call.receive<SceneUpdateRequest>()
            val scene = dao.applyChanges(request)
            repository.saveScene(scene)
            call.respond(HttpStatusCode.OK)
        }
    }
}