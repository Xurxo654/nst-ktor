package nst.api

interface SceneRepository {
    fun getScenes(): List<Scene>
    fun getSceneById(id: Int): Scene?
    fun createScene(request: SceneCreationRequest): Boolean
}