package id.co.mentalhealth.ui.auth.registrasi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import id.co.mentalhealth.data.network.ResultState
import id.co.mentalhealth.data.pref.UserPreferences
import id.co.mentalhealth.data.pref.dataStore
import id.co.mentalhealth.databinding.FragmentRegist2Binding
import id.co.mentalhealth.ui.auth.AuthViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.auth.login.LoginActivity
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.launch

class RegistFragment2 : Fragment() {
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentRegist2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegist2Binding.inflate(inflater, container, false)
        setupAnimation()

        return binding.root
    }

    private fun setupAnimation() {
        val titleAnimator =
            ObjectAnimator.ofFloat(binding.txtDaftarakun, "alpha", 1f).setDuration(150)
        val usernameAnimator =
            ObjectAnimator.ofFloat(binding.txtNamapengguna, "alpha", 1f).setDuration(100)
        val passwordAnimator =
            ObjectAnimator.ofFloat(binding.txtPassword, "alpha", 1f).setDuration(100)
        val konfirmasiAnimator = ObjectAnimator.ofFloat(binding.txtKonfPassword, "alpha", 1f)
        val registAnimator = ObjectAnimator.ofFloat(binding.btnDaftar, "alpha", 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                usernameAnimator,
                passwordAnimator,
                konfirmasiAnimator,
                registAnimator
            )
            startDelay = 100
            start()
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// hapus jika sudah baik
//        val fullName = arguments?.getString("fullName") ?: ""
//        val email = arguments?.getString("email") ?: ""
//        val age = arguments?.getString("age") ?: ""
//        val gender = arguments?.getString("gender") ?: ""

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnDaftar.setOnClickListener { lifecycleScope.launch { register() } }
    }

    private suspend fun register() {

        val fullName = arguments?.getString("fullName") ?: ""
        val email = arguments?.getString("email") ?: ""
        val age = arguments?.getString("age") ?: ""
        val gender = arguments?.getString("gender") ?: ""
        val username = binding.edtNamapengguna.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val konfirmasiPassword = binding.edtKonfPassword.text.toString().trim()

        when {
            fullName.isEmpty() || email.isEmpty() || age.isEmpty() || gender.isEmpty() -> {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Aduh!")
                    setMessage("Isi semua kolom pada halaman sebelumnya dengan lengkap")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            }

            username.isEmpty() || password.isEmpty() || konfirmasiPassword.isEmpty() -> {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Aduh!")
                    setMessage("Isi semua kolom dengan lengkap")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
                if (username.isEmpty()) {
                    binding.edtNamapengguna.error = "Username harus diisi"
                }
                if (username.length < 6) {
                    binding.edtNamapengguna.error =
                        "Username minimal 6 karakter, saat ini hanya ${username.length} karakter"
                }
                if (password.isEmpty()) {
                    binding.edtPassword.error = "Password harus diisi"
                }
                if (konfirmasiPassword.isEmpty()) {
                    binding.edtKonfPassword.error = "Konfirmasi password harus diisi"
                }
            }

            password != konfirmasiPassword -> {
                binding.edtKonfPassword.error = "Password tidak sama"
            }

            else -> {
                viewModel.register(fullName, email, age, gender, username, password)
                    .observe(requireActivity()) { response ->
                        if (response != null) {
                            when (response) {
                                is ResultState.Loading -> {
                                    showLoading(true)
                                }

                                is ResultState.Success -> {
                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("Yeah!")
                                        setMessage("Akun dengan username \"$username\" sudah jadi nih.")
                                        setPositiveButton("Lanjut") { dialog, _ ->
                                            dialog.dismiss()
                                            requireActivity().finish()
                                        }
                                        create()
                                        show()
                                    }
                                    showLoading(false)
                                }

                                is ResultState.Error -> {
                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("Aduh!")
                                        setMessage("Akun dengan username \"$username\" gagal dibuat dengan error \"${response.error}\"")
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

    private fun showLoading(isLoading: Boolean) {
        ObjectAnimator.ofFloat(binding.btnDaftar, View.ALPHA, 0.3f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}