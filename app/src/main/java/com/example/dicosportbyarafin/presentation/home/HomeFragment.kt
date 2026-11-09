package com.example.dicosportbyarafin.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.repository.Resource
import com.example.core.ui.NewsAdapter
import com.example.dicosportbyarafin.databinding.FragmentHomeBinding
import com.example.dicosportbyarafin.presentation.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsAdapter { news ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_NEWS_ID, news.id)
            }
            startActivity(intent)
        }

        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = adapter

        // Pull to refresh setup
        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.fetchNews()
        }

        homeViewModel.newsState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (!binding.swipeRefreshLayout.isRefreshing) {
                        binding.modalLoading.visibility = View.VISIBLE
                    }
                    binding.layoutError.root.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.modalLoading.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.layoutError.root.visibility = View.GONE
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.modalLoading.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.layoutError.root.visibility = View.VISIBLE
                    binding.layoutError.tvErrorMessage.text = resource.message
                }
            }
        }

        binding.layoutError.btnTryAgain.setOnClickListener {
            homeViewModel.fetchNews()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { homeViewModel.searchNews(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                homeViewModel.searchNews(newText.orEmpty())
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
