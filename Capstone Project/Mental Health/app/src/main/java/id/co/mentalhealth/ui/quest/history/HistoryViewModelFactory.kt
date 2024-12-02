package id.co.mentalhealth.ui.quest.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.di.Injection
import kotlinx.coroutines.runBlocking

class HistoryViewModelFactory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return runBlocking {
                    val repository = Injection.provideQuestRepository(context)
                    HistoryViewModel(repository)
                } as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}