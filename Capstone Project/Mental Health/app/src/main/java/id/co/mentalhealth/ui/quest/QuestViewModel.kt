package id.co.mentalhealth.ui.quest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.mentalhealth.data.network.response.Predictions
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.response.QuestionsItem
import id.co.mentalhealth.ui.QuestionRepository
import kotlinx.coroutines.launch

class QuestViewModel(private val questionRepository: QuestionRepository) : ViewModel() {

    private val _questions = MutableLiveData<Result<QuestionResponse>>()
    val questions: LiveData<Result<QuestionResponse>> get() = _questions

    private val _currentIndex = MutableLiveData<Int>(0)
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _answers = MutableLiveData<MutableList<Int>>().apply { value = MutableList(25) { 0 }  }
    val answers: LiveData<MutableList<Int>> get() = _answers

    private val _predictionResult = MutableLiveData<Result<Predictions>>() // Ganti String dengan tipe hasil yang sesuai
    val predictionResult: LiveData<Result<Predictions>> get() = _predictionResult

    private val _predictionStatus = MutableLiveData<Boolean>()
    val predictionStatus: LiveData<Boolean> get() = _predictionStatus

    fun fetchQuestions() {
        viewModelScope.launch {
            val result = questionRepository.getQuestions()
            _questions.value = result
        }
    }

    fun getCurrentQuestion(): QuestionsItem? {
        val questionsList = _questions.value?.getOrNull()?.questions
        val index = _currentIndex.value ?: 0
        return questionsList?.getOrNull(index)
    }

    fun nextQuestion() {
        val currentIndexValue = _currentIndex.value ?: 0
        _currentIndex.value = currentIndexValue + 1
    }

    fun hasNextQuestion(): Boolean {
        val questionsList = _questions.value?.getOrNull()?.questions
        val currentIndexValue = _currentIndex.value ?: 0
        return (questionsList?.size ?: 0) > (currentIndexValue + 1)
    }

    fun previousQuestion() {
        val currentIndexValue = _currentIndex.value ?: 0
        if (currentIndexValue > 0) {
            _currentIndex.value = currentIndexValue - 1
        }
    }

    fun saveSelectedAnswer(answer: Int) {
        val currentIndexValue = _currentIndex.value ?: 0
        val currentAnswers = _answers.value ?: mutableListOf()
        currentAnswers[currentIndexValue] = answer
        _answers.value = currentAnswers
    }
    fun getSelectedAnswer(): Int? {
        val currentIndexValue = _currentIndex.value ?: 0
        return _answers.value?.get(currentIndexValue)
    }

    fun getAllSelectedAnswers(): List<Int>? {
        return _answers.value
    }

    fun submitAnswersForPrediction() {
        viewModelScope.launch {
            val answersList = _answers.value
            if (answersList != null) {
                // Memanggil fungsi predict dari repository
                val result = questionRepository.predict(
                    q1 = answersList[0],
                    q2 = answersList[1],
                    q3 = answersList[2],
                    q4 = answersList[3],
                    q5 = answersList[4],
                    q6 = answersList[5],
                    q7 = answersList[6],
                    q8 = answersList[7],
                    q9 = answersList[8],
                    q10 = answersList[9],
                    q11 = answersList[10],
                    q12 = answersList[11],
                    q13 = answersList[12],
                    q14 = answersList[13],
                    q15 = answersList[14],
                    q16 = answersList[15],
                    q17 = answersList[16],
                    q18 = answersList[17],
                    q19 = answersList[18],
                    q20 = answersList[19],
                    q21 = answersList[20],
                    q22 = answersList[21],
                    q23 = answersList[22],
                    q24 = answersList[23],
                    q25 = answersList[24]
                )

                // Menangani hasil prediksi
                if (result.isSuccess) {

                    val predictionResponse = result.getOrNull()
                    val predictions = predictionResponse?.predictions
                    if (predictions != null){
                        _predictionResult.value = Result.success(predictions)
                        _predictionStatus.value = true
                    }
                    Log.d("QuestViewModel", "Prediksi berhasil")
                } else {
                    val error = result.exceptionOrNull()
                    _predictionResult.value = Result.failure(error ?: Throwable("Unknown error"))
                    Log.e("QuestViewModel", "Prediksi gagal: ${error?.message}")
                }
            } else {
                Log.e("QuestViewModel", "Jawaban tidak lengkap atau tidak valid")
            }
        }
    }

}