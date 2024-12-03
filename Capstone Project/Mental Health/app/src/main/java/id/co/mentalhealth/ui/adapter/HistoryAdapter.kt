package id.co.mentalhealth.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.co.mentalhealth.data.network.response.HistoryItem
import id.co.mentalhealth.databinding.ItemListHistoryBinding
import id.co.mentalhealth.ui.quest.DetailActivity

class HistoryAdapter : ListAdapter<HistoryItem, HistoryAdapter.ListViewHolder>(DIFF_CALLBACK) {


    class ListViewHolder(private val binding: ItemListHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryItem) {
            binding.tvDate.text = history.createdAt

            val predictions = history.predictions
            val predictionsText = """
            Level Bipolar: ${predictions.levelBipolar}
            Level OCD: ${predictions.levelOCD}
            Level Kecemasan: ${predictions.levelKecemasan}
            Level Depresi: ${predictions.levelDepresi}
            Level Skizofrenia: ${predictions.levelSkizofrenia}
        """.trimIndent()
            binding.tvDescription.text = predictionsText

            binding.tvTitle.text = history.totalConsult
            Log.d("ListEventsAdapter", "$history")
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                Log.d("ListEventsAdapter", "ID: ${history.id}")
                intent.putExtra(DetailActivity.EXTRA_HISTORY_ID, history.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}