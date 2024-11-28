package id.co.mentalhealth.ui.auth.registrasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.network.response.RegisterResponse
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import id.co.mentalhealth.ui.UserRepository

class RegistViewModel(private val userPreferences: UserPreferences): ViewModel() {

    private val registRepository = UserRepository(ApiConfig.getApiService(), userPreferences)

    val registerResult: LiveData<Result<RegisterResponse>> = registRepository.registerResult

    fun register(
        fullName: String,
        email: String,
        age: String,
        gender: String,
        username: String,
        password: String
    ) {
        registRepository.register(fullName, email, age, gender, username, password)
    }
}