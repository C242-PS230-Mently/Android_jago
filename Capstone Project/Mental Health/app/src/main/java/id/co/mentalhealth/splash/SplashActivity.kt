package id.co.mentalhealth.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.animate
import id.co.mentalhealth.MainActivity
import id.co.mentalhealth.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val delay = 3000L
    private var shortAnimationDuration = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur visibilitas awal
        binding.copyright.visibility = View.GONE

        // Animasi slide dan crossfade
        slideIn()
        crossfade()

        // Navigasi ke MainActivity setelah delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
}
