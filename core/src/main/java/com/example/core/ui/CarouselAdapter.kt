package com.example.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.R
import com.example.core.databinding.ItemCarouselBinding
import com.example.core.domain.model.NewsItem

class CarouselAdapter(private val onClick: (NewsItem) -> Unit) :
    ListAdapter<NewsItem, CarouselAdapter.CarouselViewHolder>(DIFF_CALLBACK) {

    inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCarouselBinding.bind(itemView)

        fun bind(news: NewsItem) {
            binding.tvCarouselTitle.text = news.title
            binding.root.contentDescription = "Featured News: ${news.title}"

            // Ensure ViewGroup.LayoutParams for ViewPager2 item
            itemView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            Glide.with(itemView.context)
                .load(news.imageUrl.ifEmpty { news.badgeUrl })
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.ivCarouselImage)

            itemView.setOnClickListener {
                onClick(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
