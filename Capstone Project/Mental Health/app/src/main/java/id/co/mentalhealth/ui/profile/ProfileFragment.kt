package id.co.mentalhealth.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.co.mentalhealth.R
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.pref.dataStore
import id.co.mentalhealth.databinding.FragmentProfileBinding
import id.co.mentalhealth.ui.MainActivity
import id.co.mentalhealth.ui.MainViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.profile.change.ChangePwActivity
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(requireContext())
    }

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        userPreferences = UserPreferences.getInstance(requireContext().dataStore)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showImage()
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            }
            binding.profileName.text = user.name
        }

        binding.layoutPersonalInfo.setOnClickListener {
            val intent = Intent(requireActivity(), DetailProfileActivity::class.java)
            startActivity(intent)
        }

        binding.layoutPassword.setOnClickListener {
            val intent = Intent(requireActivity(), ChangePwActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener { logout() }

    }

    private fun showImage() {
        viewModel.getSession().observe(requireActivity()) { user ->
            if (user.photo != null) {
                Glide.with(binding.root.context)
                    .load(user.photo)
                    .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(20, 0)))
                    .placeholder(R.drawable.foto_profile)
                    .error(R.drawable.foto_profile)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.foto_profile)
            }
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Yakin?")
            setMessage("Memilih \"Ya\" akan keluar dari akun ini")
            setPositiveButton("Ya") { _, _ ->
                viewModel.logout()
                val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            }
            setNegativeButton("Batal", null)
            create()
            show()
        }

    }
}
