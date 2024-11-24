package id.co.mentalhealth.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import id.co.mentalhealth.databinding.ActivityLoginBinding
import id.co.mentalhealth.ui.HomePage_Activity
import id.co.mentalhealth.ui.LupaPwActivity
import id.co.mentalhealth.ui.Registrasi_Activity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val identifier = binding.edtNamapengguna.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            when {
                identifier.isEmpty() && password.isEmpty() -> {
                    showToast("Harap isi semua kolom!")
                }

                identifier.isEmpty() -> {
                    showToast("Harap isi kolom Username/email!")
                }

                password.isEmpty() -> {
                    showToast("Harap isi kolom password!")
                }

                else -> {
                    Log.d("LoginActivity", "Melanjutkan proses login untuk identifier: $identifier")
                    loginViewModel.login(identifier, password)
                }
            }
        }

        observeLoginResult()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tvBuatAkun.setOnClickListener {
            val intent = Intent(this, Registrasi_Activity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }

        binding.btnLupapw.setOnClickListener {
            val intent = Intent(this, LupaPwActivity::class.java)
            startActivity(intent)
        }
    }


    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this, Observer { result ->
            result.onSuccess { data ->
                Log.d("LoginActivity", "Login berhasil! Token: ${data.accessToken}")
                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomePage_Activity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure { throwable ->
                    Log.e("LoginActivity", "Login gagal: ${throwable.message}")
                    showToast("Login gagal, coba lagi.")
                }

        })
    }


//    private fun errorMessage(){
//        loginViewModel.errorMessage.observe(this, Observer { errorMessage ->
//            errorMessage?.let {
//                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
