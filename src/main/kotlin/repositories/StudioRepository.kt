package nst.api.repositories

import nst.api.models.Studio

interface StudioRepository {
    fun allStudios(): List<Studio>
    fun addStudio(studio: Studio)
    fun getStudio(code: String): Studio?
}