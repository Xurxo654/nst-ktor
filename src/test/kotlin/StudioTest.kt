package nst.api

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import nst.api.models.Studio
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


class StudioTest {
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
            Studio("Web Young", "WY")
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
        val studio = Studio("VivThomas", "VT")
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

}