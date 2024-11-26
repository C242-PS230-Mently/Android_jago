package id.co.mentalhealth.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.co.mentalhealth.data.UserPreferences
import id.co.mentalhealth.data.dataStore
import id.co.mentalhealth.databinding.FragmentProfileBinding
import id.co.mentalhealth.ui.MainActivity
import id.co.mentalhealth.ui.ResetPwActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private lateinit var userPreferences: UserPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        userPreferences = UserPreferences.getInstance(requireContext().dataStore)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutPersonalInfo.setOnClickListener {
            val intent = Intent(requireActivity(), DetailProfileActivity::class.java)
            startActivity(intent)
        }

        binding.layoutPassword.setOnClickListener {
            val intent = Intent(requireActivity(), ResetPwActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        lifecycleScope.launch {
            try {
                userPreferences.clearToken()

                val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish() // Tutup semua activity sebelumnya agar tidak bisa kembali
            } catch (e: Exception) {
                // Menangani error jika terjadi masalah saat menghapus token
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
