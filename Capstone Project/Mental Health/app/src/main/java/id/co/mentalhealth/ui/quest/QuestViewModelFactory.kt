package id.co.mentalhealth.ui.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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


    }
}

