package id.co.mentalhealth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, Registrasi_Activity::class.java)
            startActivity(intent)
        }

        binding.btnMasuk.setOnClickListener {
            val intent = Intent(this, HomePage_Activity::class.java)
            startActivity(intent)
        }

    }
}