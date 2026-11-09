package com.example.dicosportbyarafin.presentation.detail

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core.ui.NewsAdapter
import com.example.dicosportbyarafin.databinding.ActivityDetailBinding
import com.example.dicosportbyarafin.presentation.model.NewsUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val newsId: String by lazy {
        intent.getStringExtra(EXTRA_NEWS_ID) ?: throw IllegalArgumentException("News ID is required")
    }

    private val detailViewModel: DetailViewModel by viewModel {
        parametersOf(newsId)
    }

    private var currentNewsUiModel: NewsUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        observeViewModel()
    }

    private fun observeViewModel() {
        detailViewModel.newsItem.observe(this) { news ->
            currentNewsUiModel = news
            setupViews(news)
        }

        detailViewModel.isFavourite.observe(this) { isFav ->
            updateBookmarkIcon(isFav)
        }

        binding.fabBookmark.setOnClickListener {
            currentNewsUiModel?.let { news ->
                it.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150).withEndAction {
                    it.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                }.start()

                val currentFav = detailViewModel.isFavourite.value == true
                detailViewModel.setFavourite(news, !currentFav)
            }
        }

        detailViewModel.relatedNews.observe(this) { relatedList ->
            (binding.rvRelated.adapter as? NewsAdapter)?.submitList(relatedList)
        }
    }

    private fun setupViews(newsItem: NewsUiModel) {
        binding.tvDetailTitle.text = newsItem.title
        binding.tvDetailDescription.text = newsItem.description
        binding.tvLeague.text = newsItem.league
        binding.tvFormed.text = "Formed: ${newsItem.formedYear}"
        binding.tvStadium.text = "Stadium: ${newsItem.stadium}"

        binding.tvDetailTitle.contentDescription = "News article title: ${newsItem.title}"

        val imageUrl = newsItem.imageUrl.ifEmpty { newsItem.badgeUrl }

        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivDetailImage)

        binding.ivDetailImage.setOnClickListener {
            showZoomImageDialog(imageUrl)
        }

        val relatedAdapter = NewsAdapter { related ->
            val intent = android.content.Intent(this, DetailActivity::class.java).apply {
                putExtra(EXTRA_NEWS_ID, related.id)
            }
            startActivity(intent)
        }

        binding.rvRelated.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRelated.adapter = relatedAdapter
    }

    private fun showZoomImageDialog(imageUrl: String) {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
            setOnClickListener { dialog.dismiss() }
        }
        Glide.with(this).load(imageUrl).into(imageView)
        dialog.setContentView(imageView)
        dialog.show()
    }

    private fun updateBookmarkIcon(isFavourite: Boolean) {
        if (isFavourite) {
            binding.fabBookmark.setImageResource(com.example.dicosportbyarafin.R.drawable.ic_bookmark_filled)
        } else {
            binding.fabBookmark.setImageResource(com.example.dicosportbyarafin.R.drawable.ic_bookmark_border)
        }
    }

    companion object {
        const val EXTRA_NEWS_ID = "extra_news_id"
    }
}
