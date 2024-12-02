package id.co.mentalhealth.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.co.mentalhealth.databinding.FragmentHomeBinding
import id.co.mentalhealth.ui.PsikologActivity
import id.co.mentalhealth.ui.quest.QuestActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getWorkshop()




        binding.btnPsikolog.setOnClickListener {
            val intent = Intent(requireActivity(), PsikologActivity::class.java)
            startActivity(intent)
        }

        binding.btnTest.setOnClickListener {
            val intent = Intent(requireActivity(), QuestActivity::class.java)
            startActivity(intent)
        }
    }
}