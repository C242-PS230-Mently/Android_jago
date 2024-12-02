package id.co.mentalhealth.ui.psikolog

import id.co.mentalhealth.data.network.response.DoctorsResponse
import id.co.mentalhealth.data.network.retrofit.ApiService
import id.co.mentalhealth.data.pref.UserPreferences

class PsikologRepository(private val apiService: ApiService, private val userPreferences: UserPreferences) {

    suspend fun getPsikolog(): Result<DoctorsResponse> {
        return try {
            val response = apiService.getDoctors()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: PsikologRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): PsikologRepository =
            instance ?: synchronized(this) {
                instance ?: PsikologRepository(apiService, userPreferences)
            }.also { instance = it }
    }


}