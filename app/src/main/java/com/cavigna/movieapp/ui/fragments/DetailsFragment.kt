package com.cavigna.movieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cavigna.movieapp.R
import com.cavigna.movieapp.databinding.FragmentDetailsBinding
import com.cavigna.movieapp.model.models.model.details.Genre
import com.cavigna.movieapp.ui.adapters.BackDropAdapter
import com.cavigna.movieapp.ui.states.UiDetailsState
import com.cavigna.movieapp.utils.fillPathTMDB
import com.cavigna.movieapp.utils.launchAndRepeatWithViewLifecycle
import com.cavigna.movieapp.utils.parseDate
import com.cavigna.movieapp.viewmodel.MainViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.NullPointerException

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var recycler: RecyclerView
    private val imageAdapter by lazy { BackDropAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        recycler = binding.recyclerImages

        this.launchAndRepeatWithViewLifecycle {
            viewModel.uiDetailsState.collect { uiDetailsState ->
                when (uiDetailsState) {
                    is UiDetailsState.Loading -> {
                        isLoading(true)
                    }

                    is UiDetailsState.Success -> {
                        isLoading()
                        val movie = uiDetailsState.movieDetail!!
                        val imageResponse = uiDetailsState.imagesResponse
                        with(binding) {

                                                        when (movie.favorite) {
                                true -> checkBoxBookMark.isChecked = true
                                false -> checkBoxBookMark.isChecked = false
                            }
                            when {
                                movie.backdropPath == null -> {
                                    appBarImage.load(movie.posterPath?.let { fillPathTMDB(it) })
                                }
                                movie.posterPath == null -> {
                                    appBarImage.setImageResource(R.drawable.ic_no_image_placeholder)
                                }
                                else -> {
                                    appBarImage.load(fillPathTMDB(movie.backdropPath!!))
                                }
                            }



                            textViewTiutloDetails.text = movie.title
                            tvOverviewDetails.text = movie.overview
                            tvRelease.text = try {
                                parseDate(movie.releaseDate)
                            } catch (e: Exception) {
                                movie.releaseDate
                            }


                            try {
                                val listOfGenres = mutableListOf("")
                                val listOfLanguages = mutableListOf<String>()
                                movie.genres.forEach {
                                    listOfGenres.add(it.name)
                                }
                                tvGenres.text =
                                    "Genres: ${
                                        listOfGenres.filter { it.isNotBlank() }.joinToString()
                                    }"

                                movie.spokenLanguages.forEach {
                                    listOfLanguages.add(it.englishName)
                                }
                                tvLanguages.text = "Languages: ${listOfLanguages.joinToString()}"

                            } catch (e: Exception) {
                                tvGenres.visibility = View.GONE
                                tvLanguages.visibility = View.GONE
                            }

                            checkBoxBookMark.setOnCheckedChangeListener { _, _ ->


                                when (movie.favorite) {
                                    true -> Toast.makeText(
                                        requireContext(),
                                        "Eliminado de Favoritos",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    false -> Toast.makeText(
                                        requireContext(),
                                        "Agregado a Favoritos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                Log.i("cosa_rara", "Cosa Rara")
                                viewModel.updateMovieDetail(movie)


                            }



                            ratingBarDetails.setOnRatingBarChangeListener { ratingBar, fl, b ->
                                viewModel.puntaje.value = 0.0
                                viewModel.puntaje.value = fl.toDouble()

                                Toast.makeText(requireContext(), fl.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }



                            button.setOnClickListener {
                                try {
                                    viewModel.postRating(movie.id)
                                    Snackbar.make(
                                        coordinatorLayoutDetails,

                                        "You rated ${movie.originalTitle} with ${viewModel.puntaje.value} stars",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        requireContext(),
                                        "No hay conexión",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }


                        }
                        recycler.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recycler.adapter = imageAdapter

                        when {
                            imageResponse?.backdrops.isNullOrEmpty() -> {
                                binding.tvGallery.visibility = View.GONE
                                recycler.visibility =View.GONE
                            }
                            else -> imageAdapter.submitList(imageResponse?.backdrops)
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
