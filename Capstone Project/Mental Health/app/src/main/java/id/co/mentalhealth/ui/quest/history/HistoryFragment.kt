package id.co.mentalhealth.ui.quest.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import id.co.mentalhealth.databinding.FragmentHistoryBinding
import id.co.mentalhealth.ui.adapter.HistoryAdapter

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory(requireContext())
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.getUserAllHistory()



        historyViewModel.history.observe(viewLifecycleOwner) { result ->
            result.onSuccess { historyResponse ->
                val historyList = historyResponse.data
                Log.d("HistoryFragment", "History: $historyList")
                if (!historyList.isNullOrEmpty()) {
                    adapter.submitList(historyList)
                }else{
                    Toast.makeText(requireContext(), "Tidak ada riwayat", Toast.LENGTH_SHORT).show()
                }
            }
            result.onFailure {
                binding.tvHistory.text = "Gagal mengambil riwayat."
            }
        }

        adapter = HistoryAdapter()

        binding.listHistoryRecycler.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        binding.listHistoryRecycler.adapter = adapter
    }



}