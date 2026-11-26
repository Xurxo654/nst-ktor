package nst.api

import nst.api.models.Studio
import org.junit.Test
import kotlin.test.assertEquals

class StudioModelTest {
    @Test
    fun `Studio should have name`() {
        val studio = Studio("GirlsWay", "GW")
        assertEquals("GirlsWay", studio.name)
    }

    @Test
    fun `Studio should have code`() {
        val studio = Studio("GirlsWay", "GW")
        assertEquals("GW", studio.code)
    }
}