package id.co.mentalhealth.ui.quest

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import id.co.mentalhealth.R
import id.co.mentalhealth.data.network.response.QuestionsItem
import id.co.mentalhealth.databinding.ActivityQuestBinding


class QuestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestBinding
    private val questViewModel: QuestViewModel by viewModels {
        QuestViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Notifications", "Notifications permission granted")
            } else {
                Log.d("Notifications", "Notifications permission rejected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

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

            if (index == 24 ) { // TOTAL_QUESTIONS = 25
                binding.btnNext.text = "Kirim"
            }else{
                binding.btnNext.text = "Selanjutnya"
            }
        }


        binding.btnNext.setOnClickListener {
            val selectedAnswer = getSelectedAnswerFromRadioGroup()
            if (selectedAnswer != null) {
                questViewModel.saveSelectedAnswer(selectedAnswer)
                if (questViewModel.hasNextQuestion()) {
                    questViewModel.nextQuestion()
                    loadCurrentQuestion()
                }else{
                    questViewModel.submitAnswersForPrediction()
                    Toast.makeText(this, "Mengirim jawaban...", Toast.LENGTH_SHORT).show()
                    logUserAnswers()

                    questViewModel.predictionResult.observe(this) { result ->
                        result.onSuccess { predictions ->
                            Log.d("QuestActivity", "Predictions: $predictions")
                                val predictionsText = predictions.username
                                val predictionsMessage = predictions.message
                                sendNotification(predictionsText, predictionsMessage)
                                val intent = Intent(this, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_HISTORY_ID, predictions.id)
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
            3 -> binding.radioGroup.check(R.id.rd1)
            2 -> binding.radioGroup.check(R.id.rd2)
            1 -> binding.radioGroup.check(R.id.rd3)
            else -> binding.radioGroup.clearCheck()
        }
    }

    private fun getSelectedAnswerFromRadioGroup(): Int? {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.rd1 -> 3
            R.id.rd2 -> 2
            R.id.rd3 -> 1
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

    private fun sendNotification(title: String, message: String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "Mently"
    }

}