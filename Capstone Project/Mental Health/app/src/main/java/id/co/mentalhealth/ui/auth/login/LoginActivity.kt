package id.co.mentalhealth.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import id.co.mentalhealth.data.network.ResultState
import id.co.mentalhealth.data.pref.UserModel
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.pref.dataStore
import id.co.mentalhealth.databinding.ActivityLoginBinding
import id.co.mentalhealth.ui.MainActivity
import id.co.mentalhealth.ui.auth.AuthViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.home.HomePage_Activity
import id.co.mentalhealth.ui.password.LupaPwActivity
import id.co.mentalhealth.ui.auth.registrasi.Registrasi_Activity
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimation()

        binding.btnLogin.setOnClickListener { lifecycleScope.launch { login() } }
        binding.btnBack.setOnClickListener { finish() }

        binding.tvBuatAkun.setOnClickListener {
            val intent = Intent(this, Registrasi_Activity::class.java)
            startActivity(intent)
        }
        binding.btnLupapw.setOnClickListener {
            val intent = Intent(this, LupaPwActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun login() {
        val identifier = binding.edtNamapengguna.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        when {
            identifier.isEmpty() || password.isEmpty() -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Aduh!")
                    setMessage("Isi semua kolom dengan lengkap")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
                if (identifier.isEmpty()) {
                    binding.edtNamapengguna.error = "Username atau Email harus diisi"
                }
                if (password.isEmpty()) {
                    binding.edtPassword.error = "Password harus diisi"
                }
            }

            else -> {
                viewModel.login(identifier, password).observe(this) { response ->
                    if (response != null) {
                        when (response) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                val name = response.data.user?.fullName.toString()
                                val token = response.data.accessToken.toString()
                                val id = response.data.user?.id.toString()
                                AlertDialog.Builder(this).apply {
                                    setTitle("Yeah!")
                                    setMessage("Anda berhasil login dengan email atau username $identifier")
                                    setPositiveButton("Lanjut") { _, _ ->
                                        val intent = Intent(context, HomePage_Activity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                                viewModel.saveSession(UserModel(id, identifier, name, token))
                                showLoading(false)
                            }

                            is ResultState.Error -> {
                                AlertDialog.Builder(this).apply {
                                    setTitle("Aduh!")
                                    setMessage("Anda gagal login dengan email atau username \"$identifier\" dengan error \"${response.error}\"")
                                    setPositiveButton("OK", null)
                                    create()
                                    show()
                                }
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAnimation() {
        val titleAnimator =
            ObjectAnimator.ofFloat(binding.txtLogin, "alpha", 1f).setDuration(150)
        val usernameAnimator =
            ObjectAnimator.ofFloat(binding.txtNamapengguna, "alpha", 1f).setDuration(100)
        val passwordAnimator =
            ObjectAnimator.ofFloat(binding.txtPassword, "alpha", 1f).setDuration(100)
        val lupapwAnimator =
            ObjectAnimator.ofFloat(binding.btnLupapw, "alpha", 1f).setDuration(100)
        val loginAnimator =
            ObjectAnimator.ofFloat(binding.btnLogin, "alpha", 1f).setDuration(100)
        val buatakunAnimator =
            ObjectAnimator.ofFloat(binding.btnBuatAkun, "alpha", 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                usernameAnimator,
                passwordAnimator,
                lupapwAnimator,
                loginAnimator,
                buatakunAnimator
            )
            startDelay = 100
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0.3f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}


