package id.co.mentalhealth.ui.quest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.ui.QuestionRepository
import kotlinx.coroutines.launch

class QuestViewModel(private val questionRepository: QuestionRepository) : ViewModel() {

    private val _questions = MutableLiveData<Result<QuestionResponse>>()
    val questions: LiveData<Result<QuestionResponse>> get() = _questions

    private val _currentIndex = MutableLiveData<Int>(0)  // Indeks pertanyaan yang sedang ditampilkan
    val currentIndex: LiveData<Int> get() = _currentIndex

    // Fungsi untuk mengambil data pertanyaan
    fun fetchQuestions() {
        viewModelScope.launch {
            val result = questionRepository.getQuestions()
            _questions.value = result
        }
    }

    // Fungsi untuk mendapatkan pertanyaan berdasarkan indeks
    fun getCurrentQuestion(questions: QuestionResponse?): String {
        val index = _currentIndex.value ?: 0
        return questions?.questions?.getOrNull(index)?.questionText ?: "Pertanyaan tidak ditemukan"
    }

    // Fungsi untuk berpindah ke pertanyaan berikutnya
    fun nextQuestion() {
        val currentIndexValue = _currentIndex.value ?: 0
        _currentIndex.value = currentIndexValue + 1  // Mengubah indeks ke pertanyaan selanjutnya
    }

    // Cek apakah masih ada pertanyaan berikutnya
    fun hasNextQuestion(questions: QuestionResponse?): Boolean {
        return (_currentIndex.value ?: 0) < (questions?.questions?.size ?: 0) - 1
    }

    fun previousQuestion() {
        val currentIndexValue = _currentIndex.value ?: 0
        if (currentIndexValue > 0) {
            _currentIndex.value = currentIndexValue - 1  // Mengurangi indeks untuk kembali ke pertanyaan sebelumnya
        }
    }
}