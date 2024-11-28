package id.co.mentalhealth.ui.auth.registrasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.data.pref.UserPreferences

class RegisterViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistViewModel::class.java)) {
            return RegistViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}