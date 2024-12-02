package id.co.mentalhealth.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import id.co.mentalhealth.databinding.FragmentHomeBinding
import id.co.mentalhealth.ui.psikolog.PsikologActivity
import id.co.mentalhealth.ui.adapter.ArticleAdapter
import id.co.mentalhealth.ui.adapter.WorkShopAdapter
import id.co.mentalhealth.ui.quest.QuestActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var WorkShopAdapter: WorkShopAdapter
    private lateinit var articleAdapter: ArticleAdapter
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
        viewModel.getArticle()

        viewModel.workShop.observe(viewLifecycleOwner){ result ->
            result.onSuccess { workshopResponse ->
                val workshopList = workshopResponse.data
                if (!workshopList.isNullOrEmpty()) {
                    WorkShopAdapter.submitList(workshopList)
                }else{
                    binding.tvWorkshop.text = "Tidak ada workshop"
                }
            }
            result.onFailure {
                binding.tvWorkshop.text = "Gagal mengambil workshop."
            }
        }

        WorkShopAdapter = WorkShopAdapter()
        binding.workshopRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.workshopRecycler.adapter = WorkShopAdapter

        viewModel.article.observe(viewLifecycleOwner){ result ->
            result.onSuccess { articleResponse ->
                val articleList = articleResponse.data
                if (!articleList.isNullOrEmpty()) {
                    articleAdapter.submitList(articleList)
                }else{
                    binding.tvWorkshop.text = "Tidak ada workshop"
                }
            }
            result.onFailure {
                binding.tvWorkshop.text = "Gagal mengambil workshop."
            }
        }

        articleAdapter = ArticleAdapter()
        binding.artikelRecycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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