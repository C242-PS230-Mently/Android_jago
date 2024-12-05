package id.co.mentalhealth.ui.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.mentalhealth.data.network.response.DashboardItem
import id.co.mentalhealth.databinding.ListWorkshopBinding

class WorkShopAdapter : ListAdapter<DashboardItem, WorkShopAdapter.ListViewHolder>(DIFF_CALLBACK) {


    class ListViewHolder(private val binding: ListWorkshopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workShop: DashboardItem) {
            Glide.with(binding.ivPicture.context)
                .load(workShop.image_url)
                .into(binding.ivPicture)
            Log.d("ListItemAdapter", "$workShop")
            itemView.setOnClickListener {
                val url = workShop.full_article_link
                if (url != null) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListWorkshopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DashboardItem>() {
            override fun areItemsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}