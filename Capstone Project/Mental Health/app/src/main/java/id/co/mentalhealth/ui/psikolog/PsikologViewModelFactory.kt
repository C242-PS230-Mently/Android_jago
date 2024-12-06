package id.co.mentalhealth.ui.psikolog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.di.Injection

class PsikologViewModelFactory(private val repository: PsikologRepository): ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PsikologViewModel::class.java) -> {
                PsikologViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PsikologViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): PsikologViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PsikologViewModelFactory::class.java) {
                    INSTANCE = PsikologViewModelFactory(Injection.provideRepository(context, PsikologRepository::class.java))
                }
            }
            return INSTANCE as PsikologViewModelFactory
        }
    }
}