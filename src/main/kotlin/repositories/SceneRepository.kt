package nst.api.repositories

import nst.api.models.SceneCreationRequest
import nst.api.models.SceneDAO

interface SceneRepository {
    fun getScenes(): List<SceneDAO>
    fun getSceneById(id: Long): SceneDAO?
    fun createScene(request: SceneCreationRequest)
    fun saveScene(scene: SceneDAO)
    fun deleteScene(id: Long)
}