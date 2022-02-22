package com.cavigna.movieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cavigna.movieapp.databinding.FragmentUpcomingBinding
import com.cavigna.movieapp.ui.adapters.MoListAdapter
import com.cavigna.movieapp.ui.adapters.MoPageAdapter
import com.cavigna.movieapp.ui.states.UiUpComingState
import com.cavigna.movieapp.utils.launchAndRepeatWithViewLifecycle
import com.cavigna.movieapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UpcomingFragment : Fragment(), MoPageAdapter.ExtraerId {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var recycler: RecyclerView
    private val adapter by lazy { MoListAdapter(this, "upcoming") }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)

        recycler = binding.recyclerUpcoming
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val uiState = viewModel.uiUpComingState
        this.launchAndRepeatWithViewLifecycle {
            uiState.collect {state->
            when(state){
                is UiUpComingState.Success -> adapter.submitList(state.movieResponse.popularMovies)
                is UiUpComingState.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }

            }
        }




        return binding.root

    }

    override fun alHacerClick(id: Int) {
        this.launchAndRepeatWithViewLifecycle {

            viewModel.idMovie.value = id
            viewModel.selectMovieDetails()

        }
    }


}