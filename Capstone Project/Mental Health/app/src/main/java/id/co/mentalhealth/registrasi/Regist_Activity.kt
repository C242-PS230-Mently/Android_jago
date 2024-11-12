package id.co.mentalhealth.registrasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.R
import id.co.mentalhealth.databinding.ActivityRegistrasiBinding

class Regist_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnLanjut.setOnClickListener {
            // Menggunakan FragmentTransaction untuk navigasi ke Fragment
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .commit()
        }
    }
}