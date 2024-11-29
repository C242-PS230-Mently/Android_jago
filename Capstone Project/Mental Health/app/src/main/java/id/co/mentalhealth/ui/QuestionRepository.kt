package id.co.mentalhealth.ui

import android.util.Log
import id.co.mentalhealth.data.UserPreferences
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.retrofit.ApiService

class QuestionRepository(private val apiService: ApiService,private val userPreferences: UserPreferences) {

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

    fun predict(
        q1: Int, q2: Int, q3: Int, q4: Int, q5: Int,
        q6: Int, q7: Int, q8: Int, q9: Int, q10: Int,
        q11: Int, q12: Int, q13: Int, q14: Int, q15: Int,
        q16: Int, q17: Int, q18: Int, q19: Int, q20: Int,
        q21: Int, q22: Int, q23: Int, q24: Int, q25: Int
    ) {

    }

}