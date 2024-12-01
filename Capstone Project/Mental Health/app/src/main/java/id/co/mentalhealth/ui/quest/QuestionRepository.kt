package id.co.mentalhealth.ui.quest

import android.util.Log
import id.co.mentalhealth.data.network.response.PredictionResponse
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.retrofit.ApiService
import id.co.mentalhealth.data.pref.UserPreferences
import retrofit2.Response

class QuestionRepository(private val apiService: ApiService,private val userPreferences: UserPreferences) {

    companion object {
        @Volatile
        private var instance: QuestionRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): QuestionRepository =
            instance ?: synchronized(this) {
                instance ?: QuestionRepository(apiService, userPreferences)
            }.also { instance = it }
    }


    suspend fun getQuestions(): Result<QuestionResponse> {
        return try {
            val response = apiService.getQuestions()
            Result.success(response)
        } catch (e: Exception) {
            Log.e("UserRepository", "Gagal mengambil pertanyaan: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun predict(
        q1: Int, q2: Int, q3: Int, q4: Int, q5: Int,
        q6: Int, q7: Int, q8: Int, q9: Int, q10: Int,
        q11: Int, q12: Int, q13: Int, q14: Int, q15: Int,
        q16: Int, q17: Int, q18: Int, q19: Int, q20: Int,
        q21: Int, q22: Int, q23: Int, q24: Int, q25: Int
    ): Result<PredictionResponse> {
        return try {
            val response: Response<PredictionResponse> = apiService.predict(
                q1, q2, q3, q4, q5, q6, q7, q8, q9, q10,
                q11, q12, q13, q14, q15, q16, q17, q18, q19, q20,
                q21, q22, q23, q24, q25
            )

            if (response.isSuccessful) {
                Log.d("QuestionRepository", "Prediksi berhasil: ${response.body()}")
                Result.success(response.body()!!)
            } else {
                Log.e("QuestionRepository", "Prediksi gagal: ${response.message()}")
                Result.failure(Exception("Prediction failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("QuestionRepository", "Gagal melakukan prediksi: ${e.message}", e)
            Result.failure(e)
        }
    }
}