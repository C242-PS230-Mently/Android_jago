package id.co.mentalhealth.ui.auth.registrasi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.co.mentalhealth.R
import id.co.mentalhealth.databinding.FragmentRegist1Binding
import id.co.mentalhealth.ui.auth.AuthViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.auth.login.LoginActivity

class RegistFragment1 : Fragment() {
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentRegist1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegist1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnLanjut.setOnClickListener {
            val fullName = binding.edtNamalengkap.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val age = binding.edtUmur.text.toString().trim()
            val gender = binding.ddJeniskelamin.text.toString().trim()

            if (fullName.isEmpty()) {
                binding.edtNamalengkap.error = "Nama lengkap harus diisi"
            } else if (email.isEmpty()) {
                binding.edtEmail.error = "Email tidak boleh kosong"
            } else if (age.isEmpty()) {
                binding.edtUmur.error = "Umur harus diisi"
            } else {
                val fragment2 = RegistFragment2()
                val bundle = Bundle()
                bundle.putString("fullName", fullName)
                bundle.putString("email", email)
                bundle.putString("age", age)
                bundle.putString("gender", gender)
                fragment2.arguments = bundle

                parentFragmentManager.beginTransaction().apply {
                    replace(
                        R.id.container_register,
                        fragment2,
                        RegistFragment2::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }
            }
        }

        binding.tvMasuk.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

    }

}