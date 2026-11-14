package nst.api

class ListStudioRepository: StudioRepository {
    private val studios = mutableListOf<Studio>(
        Studio("GirlsWay", "GW"),
        Studio("Hot and Mean", "HAM"),
        Studio("MetArt", "MA"),
        Studio("MetArtX", "MAX"),
        Studio("VivThomas", "VT")
    )
    override fun allStudios(): List<Studio> = studios

    override fun addStudio(studio: Studio) {
        studios.add(studio)
    }
}