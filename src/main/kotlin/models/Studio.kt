package nst.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Studio(val name: String, val code: String) {
}