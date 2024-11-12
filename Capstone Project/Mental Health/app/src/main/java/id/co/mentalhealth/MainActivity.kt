package id.co.mentalhealth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityMainBinding
import id.co.mentalhealth.registrasi.Regist_Activity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, Regist_Activity::class.java)
            startActivity(intent)
        }

    }
}