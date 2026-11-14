package nst.api

interface StudioRepository {
    fun allStudios(): List<Studio>
    fun addStudio(studio: Studio)
}