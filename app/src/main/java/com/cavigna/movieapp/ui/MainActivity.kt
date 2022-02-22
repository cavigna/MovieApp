package com.cavigna.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.cavigna.movieapp.R
import com.cavigna.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.popular_menu -> navController.navigate(R.id.homeFragment)
                R.id.search_menu -> navController.navigate(R.id.searchFragment)
                R.id.upcoming_menu -> navController.navigate(R.id.upcomingFragment)
                R.id.favorite_menu -> navController.navigate(R.id.favoriteFragment)
            }
            true
        }
    }
}