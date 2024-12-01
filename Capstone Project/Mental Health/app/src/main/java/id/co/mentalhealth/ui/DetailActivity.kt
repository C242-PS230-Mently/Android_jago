package id.co.mentalhealth.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.data.network.response.Predictions
import id.co.mentalhealth.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {



        private val animationDurazion = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val predictions: Predictions? = intent.getParcelableExtra<Predictions>("predictions")
        Log.d("predictions", predictions.toString())

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