package nst.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {
    routing {
        get("/studios") {
            val studios = listOf(
                Studio("GirlsWay", "GW"),
                Studio("Hot and Mean", "HAM"),
                Studio("MetArt", "MA"),
                Studio("MetArtX", "MAX"),
                Studio("VivThomas", "VT")
            )
            call.respond(studios)
        }
        staticResources("/static", "static")

    }
}