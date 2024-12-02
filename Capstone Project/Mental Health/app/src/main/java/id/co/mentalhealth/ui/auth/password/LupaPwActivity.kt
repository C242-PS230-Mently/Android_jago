package id.co.mentalhealth.ui.auth.password

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityLupaPwBinding
import id.co.mentalhealth.ui.auth.AuthViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory

class LupaPwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLupaPwBinding
    private val viewModel: AuthViewModel by viewModels(){
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.forgot.observe(this){result ->
            result.onSuccess {
                Toast.makeText(this, "Email berhasil dikirim", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ResetPwActivity::class.java)
                startActivity(intent)
            }
            result.onFailure { error ->
                Toast.makeText(this, "Gagal mengirim email: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnKirim.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            if(email.isEmpty()){
                Toast.makeText(this, "Masukkan Email Terlebih dahulu", Toast.LENGTH_SHORT).show()
            }else{
                if(isValidEmail(email)){
                    viewModel.forgot(email)
                }else{
                    Toast.makeText(this, "Masukkan Email yang Valid", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}