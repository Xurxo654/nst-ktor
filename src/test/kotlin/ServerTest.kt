package nst.api

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*

class ServerTest {

    @Test
    fun `test studio get endpoint`() = testApplication {
        // loads default configuration
        configure()
        // verify server studios returns 200
        assertEquals(HttpStatusCode.OK, client.get("/studios").status)
    }

    @Test
    fun `studios can be retrieved`() = testApplication {
        configure()
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

}
