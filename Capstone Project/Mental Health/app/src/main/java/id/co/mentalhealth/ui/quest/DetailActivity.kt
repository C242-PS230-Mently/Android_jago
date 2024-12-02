package id.co.mentalhealth.ui.quest

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.data.network.response.PredictionResponse
import id.co.mentalhealth.databinding.ActivityDetailBinding
import id.co.mentalhealth.ui.quest.history.HistoryViewModel
import id.co.mentalhealth.ui.quest.history.HistoryViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: HistoryViewModel by viewModels(){
        HistoryViewModelFactory(this)
    }

    companion object {
        const val EXTRA_HISTORY_ID = "extra_history_id"

        private val animationDurazion = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserAllHistory()

        viewModel.history.observe(this) { result ->
            if (result.isSuccess) {
                val historyList = result.getOrNull()?.data
                Log.d("DetailActivity", "Loaded History List: $historyList")
                val historyId = intent.getIntExtra(DetailActivity.EXTRA_HISTORY_ID, -1)
                Log.d("DetailActivity", "Loaded History List: $historyId")

                // Cari history item berdasarkan ID
                val historyItem = historyList?.find { it.id == historyId }
                if (historyItem != null) {
                    binding.tvDate.text = historyItem.createdAt
                    val predictions = historyItem.predictions
                    predictions?.let {
                        val updateBarSet = listOf(
                            "Skizofrenia" to getLevelValue(it.levelSkizofrenia),
                            "OCD" to getLevelValue(it.levelOCD),
                            "Kecemasan" to getLevelValue(it.levelKecemasan),
                            "Depresi" to getLevelValue(it.levelDepresi),
                            "Bipolar" to getLevelValue(it.levelBipolar)
                        )
                        binding.apply {
                            barChartHorz.animation.duration = animationDurazion
                            barChartHorz.animate(updateBarSet)
                        }
                    }
                } else {
                    Log.e("DetailActivity", "HistoryItem not found for ID $historyId")
                }
            } else {
                Log.e("DetailActivity", "Error loading history data: ${result.exceptionOrNull()}")
            }
        }

        val predictions: PredictionResponse? = intent.getParcelableExtra<PredictionResponse>("predictions")
        Log.d("predictions", predictions.toString())
        binding.tvDate.text = predictions?.createdAt
        val prediction = predictions?.predictions
        prediction?.let {
            val updateBarSet = listOf(
                "Skizofrenia" to getLevelValue(it.levelSkizofrenia),
                "OCD" to getLevelValue(it.levelOCD),
                "Kecemasan" to getLevelValue(it.levelKecemasan),
                "Depresi" to getLevelValue(it.levelDepresi),
                "Bipolar" to getLevelValue(it.levelBipolar)
            )
            binding.apply {
                barChartHorz.animation.duration = animationDurazion
                barChartHorz.animate(updateBarSet)
            }
        }
    }

    private fun getLevelValue(level: String): Float {
        return when (level) {
            "Tinggi" -> 6f
            "Sedang" -> 4f
            "Rendah" -> 2f
            else -> 0f
        }
    }
}