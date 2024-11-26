package id.co.mentalhealth.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import id.co.mentalhealth.data.UserPreferences
import id.co.mentalhealth.data.dataStore
import id.co.mentalhealth.databinding.ActivitySplashBinding
import id.co.mentalhealth.ui.MainActivity
import id.co.mentalhealth.ui.home.HomePage_Activity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private lateinit var userPreferences: UserPreferences

    private val delay = 3000L
    private var shortAnimationDuration = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences.getInstance(applicationContext.dataStore)

        // Atur visibilitas awal
        binding.copyright.visibility = View.GONE

        // Animasi slide dan crossfade
        slideIn()
        crossfade()

        // Navigasi ke MainActivity setelah delay
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, delay)
    }

    private fun crossfade() {
        binding.copyright.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null)
        }
    }

    private fun slideIn() {
        binding.logoMently.apply {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    translationY = height.toFloat()
                    animate()
                        .translationY(0f)
                        .setDuration(shortAnimationDuration)
                        .setListener(null)

                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            Log.d("SplashActivity", "Memulai pengecekan status login...")

            try {
                // Ambil token dari DataStore
                val token = userPreferences.getToken().first()

                if (token.isNullOrEmpty()) {
                    Log.d("SplashActivity", "Token tidak ditemukan. Mengarahkan ke LoginActivity.")
                    navigateToLogin()
                } else {
                    Log.d("SplashActivity", "Token ditemukan: $token. Mengarahkan ke HomePage_Activity.")
                    navigateToHome()
                }
            } catch (e: Exception) {
                Log.e("SplashActivity", "Terjadi kesalahan saat memeriksa status login: ${e.message}")
                navigateToLogin() // Jika terjadi kesalahan, anggap pengguna belum login
            }
        }
    }


    private fun navigateToHome() {
        val intent = Intent(this, HomePage_Activity::class.java)
        startActivity(intent)
        finish() // Menutup SplashActivity agar pengguna tidak bisa kembali
    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Menutup SplashActivity agar pengguna tidak bisa kembali
    }
}
