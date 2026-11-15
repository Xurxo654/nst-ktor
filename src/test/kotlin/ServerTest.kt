package nst.api

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.testing.*
import kotlin.test.*

suspend fun Application.configureTestApp() {
    dependencies {
        provide<StudioRepository> {
            ListStudioRepository()
        }
    }
    configureSerialization()
    configureRouting()
}

class ServerTest {

    @Test
    fun `test studio get endpoint`() = testApplication {
        // loads default configuration
        application {
            configureTestApp()
        }
        // verify server studios returns 200
        assertEquals(HttpStatusCode.OK, client.get("/studios").status)
    }

    @Test
    fun `studios can be retrieved`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/studios")
        val results = response.body<List<Studio>>()
        val expectedStudios = listOf(
            Studio("GirlsWay", "GW"),
            Studio("Hot and Mean", "HAM"),
            Studio("MetArt", "MA"),
            Studio("MetArtX", "MAX"),
            Studio("VivThomas", "VT")
        )
        assertContentEquals(expectedStudios, results)
    }

    @Test
    fun `new studio can be created`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val studio = Studio("Webyoung", "WY")
        val postResponse = client.post("/studios") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(studio)
        }
        assertEquals(HttpStatusCode.Created, postResponse.status)

        val getResponse = client.get("/studios")
        assertEquals(HttpStatusCode.OK, getResponse.status)

        val studios = getResponse.body<List<Studio>>()
        assertContains(studios, studio)
    }

    @Test
    fun `cannot create studio with duplicate code`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val studio = Studio("GirlsWay", "GW")
        val response = client.post("/studios") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(studio)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `should get studio by code`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/studios/GW")
        assertEquals(HttpStatusCode.OK, response.status)
        val studio = response.body<Studio>()
        assertEquals(Studio("GirlsWay", "GW"), studio)
    }

    @Test
    fun `doesn't find studio`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/studios/AB")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

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
}
