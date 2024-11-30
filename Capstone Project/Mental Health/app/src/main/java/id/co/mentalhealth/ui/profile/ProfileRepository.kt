package id.co.mentalhealth.ui.profile

import androidx.lifecycle.liveData
import com.google.gson.Gson
import id.co.mentalhealth.data.network.ResultState
import id.co.mentalhealth.data.network.response.PhotoResponse
import id.co.mentalhealth.data.network.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class ProfileRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun uploadImage(file: File) = liveData {
        emit(ResultState.Loading)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PhotoResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
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