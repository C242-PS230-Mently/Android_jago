package id.co.mentalhealth.ui.quest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.di.Injection
import kotlinx.coroutines.runBlocking


//class QuestViewModelFactory(private val context: Context) :
//    ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(QuestViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return runBlocking {
//                val repository = Injection.provideQuestRepository(context)
//                QuestViewModel(repository)
//            } as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//

class QuestViewModelFactory(private val repository: QuestionRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(QuestViewModel::class.java) -> {
                QuestViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: QuestViewModelFactory? = null
        fun getInstance(context: Context): QuestViewModelFactory {
            if (INSTANCE == null) {
                synchronized(QuestViewModelFactory::class.java) {
                    INSTANCE = QuestViewModelFactory(Injection.provideRepository(context, QuestionRepository::class.java))
                }
            }
            return INSTANCE as QuestViewModelFactory
        }
    }
}