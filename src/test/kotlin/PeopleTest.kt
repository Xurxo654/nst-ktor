package nst.api

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import nst.api.models.Person
import nst.api.models.PersonCreateRequest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class PeopleTest {
    @Test
    fun `should get person by id`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/people/1")
        assertEquals(HttpStatusCode.OK, response.status)
        val expected = Person(
            1,
            "Ashley Adams",
            "1996-08-07",
            "US",
            167,
            "Brown"
        )
        val actual = response.body<Person>()
        assertEquals(expected, actual)
    }

    @Test
    fun `should get all people`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/people")
        assertEquals(HttpStatusCode.OK, response.status)
        val expected = listOf(
            Person(
                1,
                "Ashley Adams",
                "1996-08-07",
                "US",
                167,
                "Brown"
            ),
            Person(
                2,
                "Serena Blair",
                "1988-01-25",
                "US",
                152,
                "Green"
            )
        )
        val actual = response.body<List<Person>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `should create a person with all properties`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = PersonCreateRequest(
            "Malena Morgan",
            "1991-06-23",
            "US",
            172,
            "Blue"
        )
        val response = client.post("/people") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val people = client.get("/people").body<List<Person>>()
        val malena = Person(
            3,
            "Malena Morgan",
            "1991-06-23",
            "US",
            172,
            "Blue"
        )
        assertContains(people, malena)
    }

    @Test
    fun `should create a person with only required properties`() = testApplication {
        application {
            configureTestApp()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = PersonCreateRequest(
            "Malena Morgan",
            null,
            null,
            null,
            null
        )
        val response = client.post("/people") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val people = client.get("/people").body<List<Person>>()
        val malena = Person(
            3,
            "Malena Morgan",
            null,
            null,
            null,
            null
        )
        assertContains(people, malena)
    }
}