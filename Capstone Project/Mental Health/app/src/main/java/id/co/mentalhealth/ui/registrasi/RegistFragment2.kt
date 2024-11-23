package id.co.mentalhealth.ui.registrasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import id.co.mentalhealth.databinding.FragmentRegist2Binding

class RegistFragment2 : Fragment() {

    private lateinit var binding: FragmentRegist2Binding

    private val registerViewModel: RegistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegist2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fullName = arguments?.getString("fullName") ?: ""
        val email = arguments?.getString("email") ?: ""
        val age = arguments?.getString("age") ?: ""
        val gender = arguments?.getString("gender") ?: ""

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        binding.btnDaftar.setOnClickListener {
            val username = binding.edtNamapengguna.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.edtNamapengguna.error = "Username harus diisi"
            } else if (password.isEmpty()) {
                binding.edtPassword.error = "Password harus diisi"
            } else {
                registerViewModel.register(fullName, email, age, gender, username, password)
            }
        }

        observeRegistrationResult()
    }

    private fun observeRegistrationResult() {
        registerViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Registrasi berhasil: ${it.msg}", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
            result.onFailure { throwable ->
                Toast.makeText(requireContext(), "Registrasi gagal: ${throwable.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}