package nst.api

import kotlinx.serialization.Serializable

@Serializable
data class Studio(val name: String, val code: String) {
}