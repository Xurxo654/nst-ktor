package nst.api

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.testing.*
import nst.api.models.Scene
import nst.api.models.SceneCreationRequest
import nst.api.models.SceneUpdateRequest
import nst.api.models.Studio
import nst.api.repositories.ListSceneRepository
import nst.api.repositories.ListStudioRepository
import nst.api.repositories.SceneRepository
import nst.api.repositories.StudioRepository
import kotlin.test.*

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

class SceneTest {

    @Test
    fun `should get scene`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val expected = Scene(
            1,
            "The Pledge: Part One",
            "Serena Blair takes advantage of eager pledge, Ashley Adams",
            10,
            "2016-04-29",
            Studio("Web Young", "WY")
        )
        val response = client.get("/scenes/1")
        assertEquals(HttpStatusCode.OK, response.status)
        val scene = response.body<Scene>()
        assertEquals(expected, scene)
    }

    @Test
    fun `should get all scenes`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val expectedScenes = listOf(
            Scene(
                1,
                "The Pledge: Part One",
                "Serena Blair takes advantage of eager pledge, Ashley Adams",
                10,
                "2016-04-29",
                Studio("Web Young", "WY")
            ),
            Scene(
                2,
                "The Pledge: Part Two",
                "Serena needs further convincing that Ashley is up for sisterhood.",
                10,
                "2016-05-13",
                Studio("Web Young", "WY")
            )
        )
        val response = client.get("/scenes")
        assertEquals(HttpStatusCode.OK, response.status)
        val scenes = response.body<List<Scene>>()
        assertEquals(expectedScenes, scenes)
    }

    @Test
    fun `POST scenes with only required fields creates scene`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val toCreate = SceneCreationRequest(
            title = "The Pledge: One Year Later",
            studioCode = "WY",
            releaseDate = null,
            description = null,
            rating = null
        )
        val response = client.post("/scenes") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(toCreate)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `POST scenes without required fields returns 400`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/scenes") {
           header(HttpHeaders.ContentType, ContentType.Application.Json)
           setBody("""{"title":"The Pledge: One Year Later"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)

    }

    @Test
    fun `POST scenes with optional fields creates scene`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = SceneCreationRequest(
            title = "The Pledge: One Year Later",
            studioCode = "WY",
            releaseDate = "2017-07-30",
            description = "One year later Ashley wants to move on but still succumbs to Serena.",
            rating = 9,
        )
        val response = client.post("/scenes") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val expectedScene = Scene(
            title = "The Pledge: One Year Later",
            id = 3,
            releaseDate = "2017-07-30",
            description = "One year later Ashley wants to move on but still succumbs to Serena.",
            rating = 9,
            studio = Studio("Web Young", "WY")
        )
        val getScene = client.get("/scenes/3")
        val actualScene = getScene.body<Scene>()
        assertEquals(expectedScene, actualScene)
    }

    @Test
    fun `POST with duplicate studio and title returns bad request`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = SceneCreationRequest(
            title = "The Pledge: Part One",
            studioCode = "WY",
            releaseDate = null,
            description = null,
            rating = 9,
        )
        val response = client.post("/scenes") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `PATCH updates all fields with values`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = SceneUpdateRequest("New Pledge: Part Two", "3", "yesterday")
        val response = client.patch("/scenes/2") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val scene = client.get("/scenes/2").body<Scene>()
        val expectedScene = Scene(
            title = "The Pledge: Part Two",
            id = 2,
            releaseDate = "yesterday",
            description = "New Pledge: Part Two",
            rating = 3,
            studio = Studio("Web Young", "WY")
        )
        assertEquals(expectedScene, scene)
    }

    @Test
    fun `PATCH sets all fields to null`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = SceneUpdateRequest("", "", "")
        val response = client.patch("/scenes/2") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val scene = client.get("/scenes/2").body<Scene>()
        val expectedScene = Scene(
            title = "The Pledge: Part Two",
            id = 2,
            releaseDate = null,
            description = null,
            rating = null,
            studio = Studio("Web Young", "WY")
        )
        assertEquals(expectedScene, scene)
    }

    @Test
    fun `PATCH retains original values`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = SceneUpdateRequest(null, null, null)
        val response = client.patch("/scenes/2") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val scene = client.get("/scenes/2").body<Scene>()
        val expectedScene = Scene(
            2,
            "The Pledge: Part Two",
            "Serena needs further convincing that Ashley is up for sisterhood.",
            10,
            "2016-05-13",
            Studio("Web Young", "WY")
        )
        assertEquals(expectedScene, scene)
    }

    @Test
    fun `DELETE should delete scene`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.delete("/scenes/2")
        assertEquals(HttpStatusCode.NoContent, response.status)
        val getScene = client.get("/scenes/2")
        assertEquals(HttpStatusCode.NotFound, getScene.status)
    }
}
