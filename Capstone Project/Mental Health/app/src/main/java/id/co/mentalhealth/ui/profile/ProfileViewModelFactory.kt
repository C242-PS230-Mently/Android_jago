package id.co.mentalhealth.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.di.Injection
import id.co.mentalhealth.ui.auth.AuthViewModelFactory

class ProfileViewModelFactory(private val repository: ProfileRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ProfileViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ProfileViewModelFactory::class.java) {
                    INSTANCE = ProfileViewModelFactory(Injection.provideProfileRepository(context))
                }
            }
            return INSTANCE as ProfileViewModelFactory
        }
    }
}