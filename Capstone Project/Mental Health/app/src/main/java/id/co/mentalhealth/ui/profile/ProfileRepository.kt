package id.co.mentalhealth.ui.profile

import android.util.Log
import androidx.lifecycle.liveData
import com.google.gson.Gson
import id.co.mentalhealth.data.network.ResultState
import id.co.mentalhealth.data.network.response.ChangePwResponse
import id.co.mentalhealth.data.network.response.PhotoResponse
import id.co.mentalhealth.data.network.response.ProfileResponse
import id.co.mentalhealth.data.network.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class ProfileRepository private constructor(
    private val apiService: ApiService
) {

    suspend fun editProfile(fullName: String, username: String, email: String, gender: String, age: String) =
        liveData {
            emit(ResultState.Loading)
            try {
                val successResponse = apiService.editProfile(fullName, username, email, gender, age)
                emit(ResultState.Success(successResponse))
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, ProfileResponse::class.java)
                        emit(ResultState.Error(errorResponse.msg.toString()))
                    }

                    else -> {
                        emit(ResultState.Error(e.message.toString()))
                    }
                }
            }
        }

    suspend fun getProfile() = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getProfile()
            emit(ResultState.Success(successResponse))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse =
                        Gson().fromJson(errorBody, ProfileResponse::class.java)
                    emit(ResultState.Error(errorResponse.msg.toString()))
                }

                else -> {
                    emit(ResultState.Error(e.message.toString()))
                }
            }
        }
    }

    suspend fun uploadImage(file: File) = liveData {
        emit(ResultState.Loading)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody)
            emit(ResultState.Success(successResponse))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, PhotoResponse::class.java)
                    emit(ResultState.Error(errorResponse.msg.toString()))
                }
                else -> {
                    emit(ResultState.Error(e.message.toString()))
                }
            }
        }
    }

    suspend fun changePassword(newPassword: String): Result<ChangePwResponse> {
        return try {
            val response = apiService.changePassword(newPassword)
            Log.d("ProfileRepository", "Response: $response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error: ${e.message}", e)
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: ProfileRepository? = null
        fun getInstance(
            apiService: ApiService
        ) =
            instance ?: synchronized(this) {
                instance ?: ProfileRepository(apiService)
            }.also { instance = it }
    }
}