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
import id.co.mentalhealth.data.network.response.DoctorsItem
import id.co.mentalhealth.databinding.ItemListPsikologBinding

class PsikologAdapter: ListAdapter<DoctorsItem, PsikologAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DoctorsItem>() {
            override fun areItemsTheSame(oldItem: DoctorsItem, newItem: DoctorsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DoctorsItem, newItem: DoctorsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ListViewHolder(private val binding: ItemListPsikologBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(doctorsItem: DoctorsItem) {
            binding.tvName.text = doctorsItem.name
            binding.tvSpesial.text = doctorsItem.specialization
            binding.tvLokasi.text = doctorsItem.location
            Glide.with(binding.ivEventPicture.context)
                .load(doctorsItem.image_url)
                .into(binding.ivEventPicture)
            Log.d("ListItemAdapter", "$doctorsItem")
            itemView.setOnClickListener {
                val url = doctorsItem.contact
                if (url != null) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }else{
                    Log.d("DoctorAdapter", "URL Kosong")
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemListPsikologBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

}

//itemView.setOnClickListener {
//    val url = doctorsItem.full_article_link
//    if (url != null) {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//        itemView.context.startActivity(intent)
//    }
//}