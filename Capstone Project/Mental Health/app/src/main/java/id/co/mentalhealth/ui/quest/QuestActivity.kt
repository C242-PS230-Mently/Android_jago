package id.co.mentalhealth.ui.quest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.R
import id.co.mentalhealth.data.network.response.QuestionsItem
import id.co.mentalhealth.databinding.ActivityQuestBinding
import id.co.mentalhealth.ui.DetailActivity

class QuestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestBinding
    private val questViewModel: QuestViewModel by viewModels {
        QuestViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questViewModel.fetchQuestions()

        questViewModel.questions.observe(this) { result ->
            result.onSuccess { questionResponse ->
                questViewModel.getCurrentQuestion()?.let { question ->
                    displayCurrentQuestion(question)
                    displaySelectedAnswer()
                }
            }
            result.onFailure {
                binding.tvDescription.text = "Gagal mengambil pertanyaan."
            }
        }

        questViewModel.currentIndex.observe(this){ index ->
            binding.noquest.text = "${index + 1}/25"
        }


        binding.btnNext.setOnClickListener {
            val selectedAnswer = getSelectedAnswerFromRadioGroup()
            if (selectedAnswer != null) {
                questViewModel.saveSelectedAnswer(selectedAnswer)
                if (questViewModel.hasNextQuestion()) {
                    questViewModel.nextQuestion()
                    loadCurrentQuestion()
                }else{
                    binding.btnNext.text = "Kirim"
                    questViewModel.submitAnswersForPrediction()
                    Toast.makeText(this, "Kuis selesai! Mengirim jawaban...", Toast.LENGTH_SHORT).show()
                    logUserAnswers()

                    questViewModel.predictionResult.observe(this) { result ->
                        result.onSuccess { predictions ->
                                val intent = Intent(this, DetailActivity::class.java)
                                intent.putExtra("predictions", predictions)
                                startActivity(intent)
                                finish()
                        }
                        result.onFailure {
                            Toast.makeText(this, "Gagal melakukan prediksi.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Pilih jawaban terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            questViewModel.previousQuestion()
            loadCurrentQuestion()
        }
    }


    private fun displayCurrentQuestion(question: QuestionsItem) {
        binding.tvDescription.text = question.questionText
    }

    private fun displaySelectedAnswer() {
        val selectedAnswer = questViewModel.getSelectedAnswer()
        when (selectedAnswer) {
            1 -> binding.radioGroup.check(R.id.rd1)
            2 -> binding.radioGroup.check(R.id.rd2)
            3 -> binding.radioGroup.check(R.id.rd3)
            else -> binding.radioGroup.clearCheck()
        }
    }

    private fun getSelectedAnswerFromRadioGroup(): Int? {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.rd1 -> 1
            R.id.rd2 -> 2
            R.id.rd3 -> 3
            else -> null
        }
    }

    private fun logUserAnswers() {
        val answers = questViewModel.getAllSelectedAnswers()

        answers?.forEachIndexed { index, answer ->
            Log.d("UserAnswers", "Pertanyaan ke: ${index + 1}, Jawaban: $answer")
        }

    }

    private fun loadCurrentQuestion() {
        questViewModel.getCurrentQuestion()?.let { question ->
            displayCurrentQuestion(question)
            displaySelectedAnswer()
        }
    }

}