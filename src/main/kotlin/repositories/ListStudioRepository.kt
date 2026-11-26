package nst.api.repositories

import nst.api.models.Studio

class ListStudioRepository: StudioRepository {
    private val studios = mutableListOf(
        Studio("GirlsWay", "GW"),
        Studio("Hot and Mean", "HAM"),
        Studio("MetArt", "MA"),
        Studio("MetArtX", "MAX"),
        Studio("Web Young", "WY")
    )
    override fun allStudios(): List<Studio> = studios

    override fun addStudio(studio: Studio) {
        if (studios.map { it.code }.contains(studio.code)) {
            throw IllegalArgumentException("${studio.code} already in use")
        }
        studios.add(studio)
    }

    override fun getStudio(code: String): Studio? = studios.find { it.code == code }
}