package nst.api

import kotlinx.serialization.Serializable

@Serializable
data class SceneCreationRequest(
    val title: String,
    val studioCode: String,
    val releaseDate: String?,
    val description: String?,
    val rating: Int?
) {
}