package com.cavigna.movieapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import coil.load
import com.cavigna.movieapp.databinding.FragmentDetailsBinding
import com.cavigna.movieapp.model.models.model.details.Genre
import com.cavigna.movieapp.ui.states.UiDetailsState
import com.cavigna.movieapp.utils.fillPathTMDB
import com.cavigna.movieapp.utils.launchAndRepeatWithViewLifecycle
import com.cavigna.movieapp.viewmodel.MainViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

        this.launchAndRepeatWithViewLifecycle {
            viewModel.uiDetailsState.collect { uiDetailsState ->
                when (uiDetailsState) {
                    is UiDetailsState.Loading -> {
                        isLoading(true)
                    }

                    is UiDetailsState.Success -> {
                        isLoading()
                        val movie = uiDetailsState.movieDetail!!
                        with(binding) {
                            appBarImage.load(fillPathTMDB(movie.backdropPath))
                            textViewTiutloDetails.text = movie.title
                            tvOverviewDetails.text = movie.overview
                            tvRelease.text = movie.releaseDate


                            try {
                                val listOfGenres = mutableListOf("")
                                val listOfLanguages = mutableListOf<String>()
                                movie.genres.forEach {
                                    listOfGenres.add(it.name)
                                }
                                tvGenres.text =
                                    "Genres: ${listOfGenres.filter { it.isNotBlank() }.joinToString()}"

                                movie.spokenLanguages.forEach {
                                    listOfLanguages.add(it.englishName)
                                }
                                tvLanguages.text = "Languages: ${listOfLanguages.joinToString()}"

                            }catch (e: Exception){
                                tvGenres.visibility = View.GONE
                                tvLanguages.visibility = View.GONE
                            }

                            when (movie.favorite) {
                                true -> checkBoxBookMark.isChecked = true
                                false -> checkBoxBookMark.isChecked = false
                            }

                            checkBoxBookMark.setOnCheckedChangeListener { _, _ ->
                                viewModel.updateMovieDetail(movie)
                                when(movie.favorite){
                                    true -> Toast.makeText(requireContext(), "Eliminado de Favoritos", Toast.LENGTH_SHORT).show()

                                    false -> Toast.makeText(requireContext(), "Agregado a Favoritos", Toast.LENGTH_SHORT).show()
                                }


                            }

                            ratingBarDetails.setOnRatingBarChangeListener { ratingBar, fl, b ->
                                Toast.makeText(requireContext(), fl.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    is UiDetailsState.Error -> {
                        val error = uiDetailsState.e!!

                        binding.scrollViewDetails.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        return binding.root
    }

    private fun isLoading(
        isLoading: Boolean = false,

    ) {
        binding.apply {
            when (isLoading) {
                true -> {
                    appbar.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    nestedScrollView.visibility = View.GONE
                }
                false -> {
                    appbar.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    nestedScrollView.visibility = View.VISIBLE
                }
            }
        }
    }




}

/*
 true -> Snackbar.make(container!!, "Eliminado de Favoritos", Snackbar.LENGTH_SHORT).show()
    false -> Snackbar.make(container!!, "Agregado a Favoritos", Snackbar.LENGTH_SHORT).show()
 */