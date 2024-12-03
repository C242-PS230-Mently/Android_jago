package id.co.mentalhealth.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.network.response.PasswordResponse
import id.co.mentalhealth.data.pref.UserModel
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _forgot = MutableLiveData<Result<PasswordResponse>>()
    val forgot: LiveData<Result<PasswordResponse>> = _forgot

    private val _resetPassword = MutableLiveData<Result<PasswordResponse>>()
    val resetPassword: LiveData<Result<PasswordResponse>> = _resetPassword


    suspend fun register(fullName: String, email: String, age: String, gender: String, username: String, password: String) =
        authRepository.register(fullName, email, age, gender, username, password)

    suspend fun login(email: String, password: String) = authRepository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            authRepository.saveSession(user)
        }

    }

    fun forgot(email: String) {
        viewModelScope.launch{
            val resultl = authRepository.forgotPassword(email)
            _forgot.value = resultl
        }
    }

    fun resetPassword(otp: String, newPassword: String){
        viewModelScope.launch {
            val result = authRepository.resetPassword(otp, newPassword)
            _resetPassword.value = result
        }
    }


}