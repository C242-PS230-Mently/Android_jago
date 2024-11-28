package id.co.mentalhealth.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.di.Injection
import id.co.mentalhealth.ui.MainViewModel

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): AuthViewModelFactory {
            if (INSTANCE == null) {
                synchronized(AuthViewModelFactory::class.java) {
                    INSTANCE = AuthViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as AuthViewModelFactory
        }
    }
}