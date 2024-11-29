package id.co.mentalhealth.ui.quest

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import id.co.mentalhealth.data.network.response.QuestionResponse
import id.co.mentalhealth.data.network.retrofit.ApiConfig
import id.co.mentalhealth.databinding.ActivityQuestBinding
import id.co.mentalhealth.ui.QuestionRepository

class QuestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestBinding
//    private val questViewModel: QuestViewModel by viewModels {
//        QuestViewModelFactory(QuestionRepository(ApiConfig.getApiService()))
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        questViewModel.questions.observe(this, Observer { result ->
//            result.onSuccess { questionResponse ->
//                // Menampilkan data pertanyaan
//                displayCurrentQuestion(questionResponse)
//            }
//            result.onFailure { error ->
//                Log.e("QuestActivity", "Gagal mengambil pertanyaan: ${error.message}")
//            }
//        })
//
//        // Mengambil data pertanyaan
//        questViewModel.fetchQuestions()

        // Aksi tombol "Selanjutnya"
        binding.btnNext.setOnClickListener {
//            questViewModel.nextQuestion() // Pindah ke pertanyaan selanjutnya
//            questViewModel.questions.value?.onSuccess { questionResponse ->
//                displayCurrentQuestion(questionResponse) // Tampilkan pertanyaan baru
//            }
        }

        // Aksi tombol "Kembali"
        binding.btnBack.setOnClickListener {
//            questViewModel.previousQuestion()  // Pindah ke pertanyaan sebelumnya
//            questViewModel.questions.value?.onSuccess { questionResponse ->
//                displayCurrentQuestion(questionResponse) // Tampilkan pertanyaan baru
//            }
        }
    }

    // Fungsi untuk menampilkan pertanyaan
    private fun displayCurrentQuestion(questionResponse: QuestionResponse) {
//        val currentQuestionText = questViewModel.getCurrentQuestion(questionResponse)
//        binding.tvDescription.text = currentQuestionText

        // Menyembunyikan tombol "Next" jika sudah tidak ada pertanyaan berikutnya
//        val hasNext = questViewModel.hasNextQuestion(questionResponse)
//        binding.btnNext.isEnabled = hasNext
    }
}
