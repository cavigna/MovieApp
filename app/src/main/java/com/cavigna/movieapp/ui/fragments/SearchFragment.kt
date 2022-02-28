package com.cavigna.movieapp.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cavigna.movieapp.databinding.FragmentSearchBinding
import com.cavigna.movieapp.ui.adapters.MoListAdapter
import com.cavigna.movieapp.ui.adapters.MoPageAdapter
import com.cavigna.movieapp.ui.states.UiSearchState
import com.cavigna.movieapp.utils.hideKeyboard
import com.cavigna.movieapp.utils.launchAndRepeatWithViewLifecycle
import com.cavigna.movieapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class SearchFragment : Fragment(), MoPageAdapter.ExtraerId {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recycler: RecyclerView
    private val adapter by lazy { MoListAdapter(this, "search") }
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        recycler = binding.recyclerSearch
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        searchView = binding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.fetchSearchMovie(query = query)
                    hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        this.launchAndRepeatWithViewLifecycle {
            viewModel.uiSearchState.collect {
                when (it) {
                    is UiSearchState.Loading -> isLoading(loading = true, false)
                    is UiSearchState.Success -> {
                        if (it.movieResponse.popularMovies.isNotEmpty()) {

                            isLoading(loading = false, success = true)
                            adapter.submitList(it.movieResponse.popularMovies)
                        }else{
                            isLoading(loading = false, false)

                        }
                    }
                    is UiSearchState.Error -> {
                        recycler.visibility = View.GONE
                        binding.progressBarSearch.visibility = View.GONE
                        binding.imageviewNoConection.visibility = View.VISIBLE
                    }


                }
            }
        }





        return binding.root
    }

    override fun alHacerClick(id: Int) {
        viewModel.idMovie.value = id
        viewModel.selectMovieDetails()
    }

    private fun isLoading(loading: Boolean = false, success: Boolean = false) {

        when (loading) {
            true -> {
                recycler.visibility = View.GONE
                binding.progressBarSearch.visibility = View.VISIBLE
            }
            else -> {
                when (success) {
                    true -> {
                        recycler.visibility = View.VISIBLE
                        binding.progressBarSearch.visibility = View.GONE
                        binding.imageViewNoResults.visibility = View.GONE
                    }

                    false -> {
                        recycler.visibility = View.GONE
                        binding.progressBarSearch.visibility = View.GONE
                        binding.imageViewNoResults.visibility = View.VISIBLE
                    }
                }

            }

        }


    }


}

