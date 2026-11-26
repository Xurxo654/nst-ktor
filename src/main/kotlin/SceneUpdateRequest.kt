package nst.api

import kotlinx.serialization.Serializable

@Serializable
data class SceneUpdateRequest(val description: String?, val rating: String?, val releaseDate: String?)
