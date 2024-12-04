package id.co.mentalhealth.ui.auth.registrasi

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.co.mentalhealth.R
import id.co.mentalhealth.databinding.FragmentRegist1Binding
import id.co.mentalhealth.ui.auth.AuthViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.auth.login.LoginActivity
import kotlinx.coroutines.NonCancellable.start

class RegistFragment1 : Fragment() {
    private lateinit var binding: FragmentRegist1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegist1Binding.inflate(layoutInflater)
        setupAnimation()

        return binding.root
    }

    private fun setupAnimation() {
        val titleAnimator =
            ObjectAnimator.ofFloat(binding.txtDaftarakun, "alpha", 1f).setDuration(150)
        val nameAnimator =
            ObjectAnimator.ofFloat(binding.txtNamalengkap, "alpha", 1f).setDuration(100)
        val emailAnimator =
            ObjectAnimator.ofFloat(binding.txtEmail, "alpha", 1f).setDuration(100)
        val ageAnimator =
            ObjectAnimator.ofFloat(binding.txtUmur, "alpha", 1f).setDuration(100)
        val genderAnimator =
            ObjectAnimator.ofFloat(binding.txtJeniskelamin, "alpha", 1f).setDuration(100)
        val nextAnimator =
            ObjectAnimator.ofFloat(binding.btnLanjut, "alpha", 1f).setDuration(100)
        val containerAnimator =
            ObjectAnimator.ofFloat(binding.linearLayout2, "alpha", 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                nameAnimator,
                emailAnimator,
                ageAnimator,
                genderAnimator,
                nextAnimator,
                containerAnimator
            )
            startDelay = 100
            start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnLanjut.setOnClickListener { next() }

        binding.tvMasuk.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun next() {
        val fullName = binding.edtNamalengkap.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val age = binding.edtUmur.text.toString().trim()
        val gender = binding.ddJeniskelamin.text.toString().trim()

        when {
            fullName.isEmpty() || email.isEmpty() || age.isEmpty() || gender.isEmpty() -> {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Aduh!")
                    setMessage("Isi semua kolom dengan lengkap")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
                if (fullName.isEmpty()) {
                    binding.edtNamalengkap.error = "Nama lengkap harus diisi"
                }
                if (fullName.length < 5) {
                    binding.edtNamalengkap.error = "Nama lengkap minimal 5 karakter, saat ini hanya ${fullName.length} karakter"
                }
                if (email.isEmpty()) {
                    binding.edtEmail.error = "Email tidak boleh kosong"
                }
                if (age.isEmpty()) {
                    binding.edtUmur.error = "Umur harus diisi"
                }
                if (gender.isEmpty()) {
                    binding.ddJeniskelamin.error = "Jenis kelamin harus dipilih"
                }
            }

            else -> {
                val bundle = Bundle().apply {
                    putString("fullName", fullName)
                    putString("email", email)
                    putString("age", age)
                    putString("gender", gender)
                }
                val registFragment2 = RegistFragment2().apply { arguments = bundle }

                parentFragmentManager.beginTransaction().apply {
                    replace(
                        R.id.container_register,
                        registFragment2,
                        RegistFragment2::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }
            }
        }

    }
}