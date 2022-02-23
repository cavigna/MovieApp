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
import com.cavigna.movieapp.ui.states.UiDetailsState
import com.cavigna.movieapp.utils.fillPathTMDB
import com.cavigna.movieapp.utils.launchAndRepeatWithViewLifecycle
import com.cavigna.movieapp.viewmodel.MainViewModel
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
            viewModel.uiDetailsState.collect {
                when (it) {
                    is UiDetailsState.Loading -> {
                        isLoading(true)
                    }

                    is UiDetailsState.Success -> {
                        isLoading()
                        val movie = it.movieDetail!!
                        with(binding) {
                            imageViewPosterDetails.load(fillPathTMDB(movie.backdropPath))
                            textViewTiutloDetails.text = movie.title
                            tvOverviewDetails.text = movie.overview
                            tvRelease.text = movie.releaseDate

                            when (movie.favorite) {
                                true -> checkBoxBookMark.isChecked = true
                                false -> checkBoxBookMark.isChecked = false
                            }

                            checkBoxBookMark.setOnCheckedChangeListener { _, _ ->

                                when(movie.favorite){
                                    true -> Snackbar.make(container!!, "Agregado a Favoritos", Snackbar.LENGTH_SHORT).show()
                                    false -> Snackbar.make(container!!, "Eliminado de Favoritos", Snackbar.LENGTH_SHORT).show()
                                }
                                viewModel.updateMovieDetail(movie)
                                //val snackbar = Snackbar.make(container!!, "Agregado a Favoritos", Snackbar.LENGTH_SHORT)

                                //snackbar.setAnchorView(b)


                            }

                            ratingBarDetails.setOnRatingBarChangeListener { ratingBar, fl, b ->
                                Toast.makeText(requireContext(), fl.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        //Log.v("algo", "https://image.tmdb.org/t/w500/${movie.posterPath}")
                    }

                    is UiDetailsState.Error -> {
                        val error = it.e!!

                        binding.scrollViewDetails.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        this.launchAndRepeatWithViewLifecycle {

            //viewModel.imagesDetails.collect {Log.v("algo", it.toString())      }
        }

        return binding.root
    }

    private fun isLoading(
        isLoading: Boolean = false,
        progressBar: ProgressBar = binding.progressBar,
        scrollView: ScrollView = binding.scrollViewDetails
    ) {
        when (isLoading) {
            true -> {
                scrollView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            false -> {
                scrollView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }


}

