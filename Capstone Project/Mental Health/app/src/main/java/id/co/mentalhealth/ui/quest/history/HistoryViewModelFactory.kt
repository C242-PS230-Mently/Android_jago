package id.co.mentalhealth.ui.quest.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.di.Injection
import id.co.mentalhealth.ui.quest.QuestionRepository
import kotlinx.coroutines.runBlocking

//class HistoryViewModelFactory(private val context: Context): ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return runBlocking {
//                    val repository = Injection.provideQuestRepository(context)
//                    HistoryViewModel(repository)
//                } as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//}

class HistoryViewModelFactory(private val repository: QuestionRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): HistoryViewModelFactory {
            if (INSTANCE == null) {
                synchronized(HistoryViewModelFactory::class.java) {
                    INSTANCE = HistoryViewModelFactory(Injection.provideRepository(context, QuestionRepository::class.java))
                }
            }
            return INSTANCE as HistoryViewModelFactory
        }
    }
}