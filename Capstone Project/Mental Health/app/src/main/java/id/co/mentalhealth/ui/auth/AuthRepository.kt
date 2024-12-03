package id.co.mentalhealth.ui.auth

import android.util.Log
import androidx.lifecycle.liveData
import com.google.gson.Gson
import id.co.mentalhealth.data.network.ResultState
import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.response.PasswordResponse
import id.co.mentalhealth.data.network.response.RegisterResponse
import id.co.mentalhealth.data.network.retrofit.ApiService
import id.co.mentalhealth.data.pref.UserModel
import id.co.mentalhealth.data.pref.UserPreferences
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository(private val apiService: ApiService, private val userPreferences: UserPreferences) {
    suspend fun register(fullName: String, email: String, age: String, gender: String, username: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(fullName, email, age, gender, username, password)
            emit(ResultState.Success(successResponse.msg))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.msg))
        }
    }

    suspend fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            Log.d("AuthRepository", "Login Response: $successResponse")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.msg))
        }
    }

    suspend fun forgotPassword(email: String): Result<PasswordResponse> {
        return try {
            val response = apiService.forgotPassword(email)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(otp: String, newPassword: String): Result<PasswordResponse> {
        return try {
            val response = apiService.resetPassword(otp, newPassword)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
        Log.d("SessionModel", "Session saved: $user")
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreferences)
            }.also { instance = it }
    }
}