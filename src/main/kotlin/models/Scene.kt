package nst.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Scene(
    val id: Long,
    val title: String,
    val description: String?,
    val rating: Int?,
    val releaseDate: String?,
    val studio: Studio
)

@Serializable
data class SceneCreationRequest(
    val title: String,
    val studioCode: String,
    val releaseDate: String?,
    val description: String?,
    val rating: Int?
)

@Serializable
data class SceneUpdateRequest(val description: String?, val rating: String?, val releaseDate: String?)