package com.example.favourite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.ui.NewsAdapter
import com.example.favourite.databinding.FragmentFavouriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val favouriteViewModel: FavouriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsAdapter { news ->
            val intent = Intent().setClassName(requireContext(), "com.example.dicosportbyarafin.presentation.detail.DetailActivity").apply {
                putExtra("extra_news_id", news.id)
            }
            startActivity(intent)
        }

        binding.rvFavourite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavourite.adapter = adapter

        binding.progressIndicator.visibility = View.VISIBLE

        favouriteViewModel.favouriteNews.observe(viewLifecycleOwner) { list ->
            binding.progressIndicator.visibility = View.GONE
            adapter.submitList(list)
            if (list.isNullOrEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvFavourite.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvFavourite.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        binding.rvFavourite.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
