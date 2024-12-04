package id.co.mentalhealth.ui.profile.change

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.co.mentalhealth.databinding.ActivityChangePwBinding
import id.co.mentalhealth.ui.MainActivity
import id.co.mentalhealth.ui.profile.ProfileFragment
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
                Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure { error ->
                Toast.makeText(this, "Gagal mengubah password: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSimpan.setOnClickListener {
            val newPassword = binding.edtPassword.text
            if (newPassword == null) {
                Toast.makeText(this, "Masukkan Password Baru", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.changePassword(newPassword.toString().trim())
            }
        }
    }
}