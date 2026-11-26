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

fun SceneDAO.applyChanges(update: SceneUpdateRequest): SceneDAO {
    val description = when {
        update.description == null -> this.description
        update.description.isEmpty() -> null
        else -> update.description
    }
    val rating = when {
        update.rating == null -> this.rating
        update.rating.isEmpty() -> null
        else -> update.rating.toInt()
    }
    val releaseDate = when {
        update.releaseDate == null -> this.releaseDate
        update.releaseDate.isEmpty() -> null
        else -> update.releaseDate
    }
    return this.copy(description = description, rating = rating, releaseDate = releaseDate)
}