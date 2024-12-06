package id.co.mentalhealth.ui.profile.change

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityChangePwBinding
import id.co.mentalhealth.ui.profile.ProfileViewModel
import id.co.mentalhealth.ui.profile.ProfileViewModelFactory

class ChangePwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePwBinding
    private val viewModel: ProfileViewModel by viewModels(){
        ProfileViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.resetPassword.observe(this){ result ->
            result.onSuccess {
                showLoading(false)
                Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure { error ->
                showLoading(false)
                Toast.makeText(this, "Gagal mengubah password: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSimpan.setOnClickListener {
            showLoading(true)
            val newPassword = binding.edtPassword.text
            if (newPassword == null) {
                Toast.makeText(this, "Masukkan Password Baru", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.changePassword(newPassword.toString().trim())
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
}