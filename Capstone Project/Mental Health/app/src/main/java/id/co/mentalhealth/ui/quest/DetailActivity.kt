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
                    Log.d("DetailActivity", "Loaded History Item: $predictions")
                    predictions?.let {
                        val updateBarSet = listOf(
                            "TINGGI" to 10f,
                            "Skizofrenia" to getLevelInt(it.levelSkizofrenia),
                            "OCD" to getLevelInt(it.levelOCD),
                            "Kecemasan" to getLevelInt(it.levelKecemasan),
                            "Depresi" to getLevelInt(it.levelDepresi),
                            "Bipolar" to getLevelInt(it.levelBipolar),
                            "RENDAH" to 0f
                        )
                        Log.d("DetailPredic1", "Loaded History predictions Item: $updateBarSet")
                        binding.apply {
                            tvTitleDesc.text = predictions.namaSolusi
                            tvDesc.text = predictions.solusi
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
                "Tinggi" to 10f,
                "Skizofrenia" to getLevelInt(it.levelSkizofrenia),
                "OCD" to getLevelInt(it.levelOCD),
                "Kecemasan" to getLevelInt(it.levelKecemasan),
                "Depresi" to getLevelInt(it.levelDepresi),
                "Bipolar" to getLevelInt(it.levelBipolar),
                "Rendah" to 0f
            )
            Log.d("DetailPredic2", "Loaded History predictions Item: $updateBarSet")
            binding.apply {
                tvTitleDesc.text = prediction.namaSolusi
                tvDesc.text = prediction.solusi
                barChartHorz.animation.duration = animationDurazion
                barChartHorz.animate(updateBarSet)
            }
        }
    }

    private fun getLevelInt(level: Int): Float {
        return when (level) {
            3 -> 10F
            2 -> 6F
            1 -> 3F
            else -> 0f
        }
    }

    private fun getLevelString(level: String): Float {
        return when (level) {
            "3" -> 10F
            "2" -> 6F
            "1" -> 3F
            else -> 0f
        }
    }
}