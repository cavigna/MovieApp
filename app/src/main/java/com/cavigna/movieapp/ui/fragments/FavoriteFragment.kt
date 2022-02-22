package com.cavigna.movieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cavigna.movieapp.databinding.FragmentFavoriteBinding
import com.cavigna.movieapp.ui.adapters.DetailsListAdapter
import com.cavigna.movieapp.ui.states.UiFavoriteState
import com.cavigna.movieapp.utils.launchAndRepeatWithViewLifecycle
import com.cavigna.movieapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoriteFragment() : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var recycler: RecyclerView
    private val adapter by lazy { DetailsListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)


        recycler = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter =adapter

        this.launchAndRepeatWithViewLifecycle {
            viewModel.selectFavoriteMovies()
            viewModel.uiFavoriteState.collectLatest{
                when(it){
                    is UiFavoriteState.Success -> {
                        adapter.submitList(it.listOfFavorites)
                        Log.v("pruebas", it.listOfFavorites.toString())
                    }
                }
            }
        }




        return binding.root
    }
}