package id.co.mentalhealth.ui.quest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.di.Injection
import id.co.mentalhealth.ui.QuestionRepository


class QuestViewModelFactory(private val questionRepository: QuestionRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(QuestViewModel::class.java) -> {
                QuestViewModel(questionRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: QuestViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): QuestViewModelFactory {
            if (INSTANCE == null) {
                synchronized(QuestViewModelFactory::class.java) {
                    INSTANCE = QuestViewModelFactory(Injection.provideQuestRepository(context))
                }
            }
            return INSTANCE as QuestViewModelFactory
        }

    }
}

