package id.co.mentalhealth.ui

import android.util.Log
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.retrofit.ApiService

class QuestionRepository(private val apiService: ApiService) {

    // Fungsi untuk mengambil pertanyaan tanpa menggunakan token
    suspend fun getQuestions(): Result<QuestionResponse> {
        return try {
            val response = apiService.getQuestions() // Memanggil suspend function
            Result.success(response)
        } catch (e: Exception) {
            Log.e("UserRepository", "Gagal mengambil pertanyaan: ${e.message}", e)
            Result.failure(e)
        }
    }

}