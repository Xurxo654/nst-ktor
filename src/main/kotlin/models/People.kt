package nst.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: Long,
    val name: String,
    val birthDate: String?,
    val birthCountry: String?,
    val heightCM: Int?,
    val eyeColor: String?
)

@Serializable
data class PersonCreateRequest(
    val name: String,
    val birthDate: String?,
    val birthCountry: String?,
    val heightCM: Int?,
    val eyeColor: String?)