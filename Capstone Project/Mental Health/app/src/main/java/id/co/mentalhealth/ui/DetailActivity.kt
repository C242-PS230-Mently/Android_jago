package id.co.mentalhealth.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding?=null
    private val binding get() = _binding!!

    companion object {
        private val barSetHorz = listOf(
            "Bipolar" to 4f,
            "Depresi" to 5f,
            "Kecemasan" to 6f,
            "OCD" to 3f,
            "Skizofrenia" to 1f
        )

        private val animationDurazion = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            barChartHorz.animation.duration = animationDurazion
            barChartHorz.animate(barSetHorz)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}