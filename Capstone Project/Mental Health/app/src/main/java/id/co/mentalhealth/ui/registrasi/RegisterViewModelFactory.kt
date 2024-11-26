package id.co.mentalhealth.ui.registrasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.data.UserPreferences

class RegisterViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistViewModel(userPreferences) as T
    }
}