package nst.api

import kotlinx.serialization.Serializable

@Serializable
data class Scene(
    val title: String,
    val description: String,
    val rating: Int,
    val releaseDate: String,
    val studio: Studio
)