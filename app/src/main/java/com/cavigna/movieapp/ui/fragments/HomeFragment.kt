package com.cavigna.movieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cavigna.movieapp.databinding.FragmentHomeBinding
import com.cavigna.movieapp.ui.adapters.MoListAdapter
import com.cavigna.movieapp.ui.adapters.MoPageAdapter
import com.cavigna.movieapp.ui.states.UiHomeState
import com.cavigna.movieapp.utils.internetCheck
import com.cavigna.movieapp.utils.launchAndRepeatWithViewLifecycle
import com.cavigna.movieapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(), MoPageAdapter.ExtraerId {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recycler: RecyclerView
    private val adapter by lazy { MoListAdapter(this) }
    private val pageAdapter by lazy { MoPageAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        recycler = binding.recyclerHome
        recycler.layoutManager = LinearLayoutManager(requireContext())

        if (internetCheck(requireContext())) {
            initMoviePager()
        } else {
            initListAdapter()

        }

        return binding.root
    }

    override fun alHacerClick(id: Int) {
        this.launchAndRepeatWithViewLifecycle {
            viewModel.idMovie.value = id
            viewModel.selectMovieDetails()
        }
    }

    private fun initMoviePager() {
        recycler.adapter = pageAdapter
        this.launchAndRepeatWithViewLifecycle {
            viewModel.moviesPager.collect {
                pageAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    private fun initListAdapter() {
        recycler.adapter = adapter
        binding.linearConnectionHome.visibility = View.VISIBLE
        recycler.visibility = View.GONE

        Toast.makeText(requireContext(), "Mostrando resultados Offline", Toast.LENGTH_SHORT).show()

        this.launchAndRepeatWithViewLifecycle {
            delay(900)
            binding.linearConnectionHome.visibility = View.GONE
            recycler.visibility = View.VISIBLE
            viewModel.selectMovie()
            viewModel.uiHomeState.collectLatest { uiHomeState: UiHomeState ->
                when (uiHomeState) {
                    is UiHomeState.Local -> {

                        adapter.submitList(uiHomeState.movies)
                    }
                }
            }
            binding.linearConnectionHome.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }

}

