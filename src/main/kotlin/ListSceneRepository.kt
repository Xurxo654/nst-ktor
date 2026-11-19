package nst.api

class ListSceneRepository: SceneRepository {
    private val scenes: MutableList<SceneDAO> = mutableListOf(
        SceneDAO(1,
            "The Pledge: Part One",
            "WY",
            "Serena Blair takes advantage of eager pledge, Ashley Adams",
            10,
            "2016-04-29"),
        SceneDAO(2,
            "The Pledge: Part Two",
            "WY",
            "Serena needs further convincing that Ashley is up for sisterhood.",
            10,
            "2016-05-13")
    )

    override fun getScenes(): List<SceneDAO> = scenes

    override fun createScene(request: SceneCreationRequest){
        val id = scenes.maxBy { it.id }.id + 1
        val scene = SceneDAO(
            id,
            request.title,
            request.studioCode,
            request.description,
            request.rating,
            request.releaseDate
        )
        scenes.add(scene)
    }
    override fun getSceneById(id: Long): SceneDAO? = scenes.find { it.id  == id }
}