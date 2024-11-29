package id.co.mentalhealth.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.pref.UserModel
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(fullName: String, email: String, age: String, gender: String, username: String, password: String) =
        authRepository.register(fullName, email, age, gender, username, password)

    fun login(email: String, password: String) = authRepository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            authRepository.saveSession(user)
        }

    }
}