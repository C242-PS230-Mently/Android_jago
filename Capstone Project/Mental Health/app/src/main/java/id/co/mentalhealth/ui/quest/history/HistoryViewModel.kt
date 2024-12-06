package id.co.mentalhealth.ui.quest.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.network.response.HistoryResponse
import id.co.mentalhealth.ui.quest.QuestionRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val questionRepository: QuestionRepository): ViewModel() {

    private val _history = MutableLiveData<Result<HistoryResponse>>()
    val history: LiveData<Result<HistoryResponse>> get() = _history

    fun getUserAllHistory(){
        viewModelScope.launch {
            val result = questionRepository.getHistory()
            _history.value = result
        }
    }
}