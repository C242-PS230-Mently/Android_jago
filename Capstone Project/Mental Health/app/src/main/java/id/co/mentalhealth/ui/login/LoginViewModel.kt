package id.co.mentalhealth.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.co.mentalhealth.data.UserPreferences
import id.co.mentalhealth.data.network.response.LoginResponse
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import id.co.mentalhealth.ui.UserRepository

class LoginViewModel(private val userPreferences: UserPreferences): ViewModel() {

    private val loginRepository = UserRepository(ApiConfig.getApiService(), userPreferences)

    // LiveData to observe the result of login request
    val loginResult: LiveData<Result<LoginResponse>> = loginRepository.loginResult
    val errorMessage: LiveData<String?> = loginRepository.errorMessage

    // Function to call login API via the repository
    fun login(identifier: String, password: String) {
        loginRepository.login(identifier, password)
    }
}