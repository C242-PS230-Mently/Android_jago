package id.co.mentalhealth.ui.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mentalhealth.ui.QuestionRepository


class QuestViewModelFactory(private val questionRepository: QuestionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestViewModel::class.java)) {
            return QuestViewModel(questionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}