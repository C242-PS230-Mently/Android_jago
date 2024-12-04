package id.co.mentalhealth.ui.home

import id.co.mentalhealth.data.network.response.ArticleResponse
import id.co.mentalhealth.data.network.response.WorkshopResponse
import id.co.mentalhealth.data.network.retrofit.ApiService
import id.co.mentalhealth.data.pref.UserPreferences

class HomeRepository(private val apiService: ApiService,private val userPreferences: UserPreferences) {

    suspend fun getWorkshop(): Result<WorkshopResponse> {
        return try {
            val response = apiService.getWorkshop()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArticle(): Result<ArticleResponse> {
        return try {
            val response = apiService.getArticle()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: HomeRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): HomeRepository =
            instance ?: synchronized(this) {
                instance ?: HomeRepository(apiService, userPreferences)
            }.also { instance = it }
    }



}