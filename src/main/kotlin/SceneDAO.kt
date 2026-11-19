package nst.api

data class SceneDAO (
    val id : Long,
    val title: String,
    val studioCode: String,
    val description: String?,
    val rating: Int?,
    val releaseDate: String?
) {
}