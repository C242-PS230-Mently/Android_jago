package id.co.mentalhealth.registrasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.co.mentalhealth.R
import id.co.mentalhealth.databinding.FragmentRegist1Binding

class RegistFragment1 : Fragment() {

    private lateinit var binding: FragmentRegist1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegist1Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnLanjut.setOnClickListener {
            val fragment2 = RegistFragment2()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
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
}