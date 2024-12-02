package id.co.mentalhealth.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.pref.dataStore
import id.co.mentalhealth.databinding.FragmentProfileBinding
import id.co.mentalhealth.ui.MainActivity
import id.co.mentalhealth.ui.MainViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.auth.password.ResetPwActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val viewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(requireContext())
    }

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

        binding.btnLogout.setOnClickListener { logout() }

    }

    private fun logout() {
        viewModel.logout()
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish() // Tutup semua activity sebelumnya agar tidak bisa kembali
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
