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

//    fun getHistoryById(id: Int) {
//        // Cek apakah _history sudah terisi
//        _history.value?.let { result ->
//            if (result.isSuccess) {
//                val historyList = result.getOrNull()?.data
//                val historyItem = historyList?.find { it.id == id }
//                // Update LiveData dengan hasil pencarian
//                Log.d("HistoryViewModel", "Found HistoryItem: $historyItem")
//                if (historyItem != null) {
//                    _history.value = Result.success(HistoryResponse(listOf(historyItem), "Success"))
//                } else {
//                    Log.e("HistoryViewModel", "HistoryItem not found for ID: $id")
//                    _history.value = Result.success(HistoryResponse(emptyList(), "HistoryItem not found"))
//                }
//            } else {
//                Log.e("HistoryViewModel", "Error fetching history data: ${result.exceptionOrNull()}")
//                _history.value = Result.failure(result.exceptionOrNull() ?: Throwable("Unknown error"))
//            }
//        } ?: run {
//            Log.e("HistoryViewModel", "_history.value is null")
//            _history.value = Result.failure(Throwable("History data is null"))
//        }
//    }


}