package id.co.mentalhealth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.co.mentalhealth.HomePage_Activity
import id.co.mentalhealth.R
import id.co.mentalhealth.Registrasi_Activity
import id.co.mentalhealth.databinding.ActivityLoginBinding
import id.co.mentalhealth.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, HomePage_Activity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tvBuatAkun.setOnClickListener {
            val intent = Intent(this, Registrasi_Activity::class.java)
            startActivity(intent)
        }
    }
}
