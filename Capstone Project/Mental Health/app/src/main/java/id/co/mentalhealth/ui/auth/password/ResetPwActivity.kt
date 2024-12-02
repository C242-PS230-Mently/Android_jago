package id.co.mentalhealth.ui.auth.password

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityResetPwBinding
import id.co.mentalhealth.ui.auth.AuthViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.auth.login.LoginActivity

class ResetPwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPwBinding
    private val viewModel: AuthViewModel by viewModels(){
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.resetPassword.observe(this){result ->
            result.onSuccess {
                Toast.makeText(this, "Berhasil Membuat Password Baru", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            result.onFailure { error ->
                Toast.makeText(this, "Gagal Membuat Password Baru", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSimpan.setOnClickListener {
            val otp = binding.edtOtp.text.toString().trim()
            val newPassword = binding.edtPassword.text.toString().trim()
            when{
                otp.isEmpty() || newPassword.isEmpty() -> {
                    Toast.makeText(this, "Isi semua kolom dengan lengkap", Toast.LENGTH_SHORT).show()
                    if (otp.isEmpty()) {
                        Toast.makeText(this,"Masukkan OTP yang Valid", Toast.LENGTH_SHORT).show()
                    }
                    if (newPassword.isEmpty()) {
                        Toast.makeText(this, "Masukkan Password Baru", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    viewModel.resetPassword(otp, newPassword)
                }
            }
        }

    }
}