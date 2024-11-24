package id.co.mentalhealth.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityLupaPwBinding

class LupaPwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLupaPwBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}