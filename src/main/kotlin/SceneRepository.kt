package nst.api

interface SceneRepository {
    fun getScenes(): List<SceneDAO>
    fun getSceneById(id: Long): SceneDAO?
    fun createScene(request: SceneCreationRequest)
}