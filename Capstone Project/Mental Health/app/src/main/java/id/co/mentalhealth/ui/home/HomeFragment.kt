package id.co.mentalhealth.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.mentalhealth.R
import id.co.mentalhealth.databinding.FragmentHomeBinding
import id.co.mentalhealth.ui.MainActivity
import id.co.mentalhealth.ui.MainViewModel
import id.co.mentalhealth.ui.adapter.ArticleAdapter
import id.co.mentalhealth.ui.adapter.WorkShopAdapter
import id.co.mentalhealth.ui.auth.AuthViewModel
import id.co.mentalhealth.ui.auth.AuthViewModelFactory
import id.co.mentalhealth.ui.auth.login.LoginActivity
import id.co.mentalhealth.ui.psikolog.PsikologActivity
import id.co.mentalhealth.ui.quest.QuestActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var WorkShopAdapter: WorkShopAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory.getInstance(requireContext())
    }
    private val mainViewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            }
            binding.name.text = getString(R.string.greeting, user.name)
        }

        viewModel.getWorkshop()
        viewModel.getArticle()

        viewModel.workShop.observe(viewLifecycleOwner) { result ->
            result.onSuccess { workshopResponse ->
                val workshopList = workshopResponse.data
                if (!workshopList.isNullOrEmpty()) {
                    WorkShopAdapter.submitList(workshopList)
                } else {
                    binding.tvWorkshop.text = "Tidak ada workshop"
                }
            }
            result.onFailure {
                binding.tvWorkshop.text = "Gagal mengambil workshop."
            }
        }

        WorkShopAdapter = WorkShopAdapter()
        binding.workshopRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.workshopRecycler.adapter = WorkShopAdapter

        viewModel.article.observe(viewLifecycleOwner) { result ->
            result.onSuccess { articleResponse ->
                val articleList = articleResponse.data
                if (!articleList.isNullOrEmpty()) {
                    articleAdapter.submitList(articleList)
                } else {
                    binding.tvWorkshop.text = "Tidak ada workshop"
                }
            }
            result.onFailure {
                binding.tvWorkshop.text = "Gagal mengambil workshop."
            }
        }

        articleAdapter = ArticleAdapter()
        binding.artikelRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.artikelRecycler.adapter = articleAdapter



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