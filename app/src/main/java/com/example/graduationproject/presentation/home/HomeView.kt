package com.example.graduationproject.presentation.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeView : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent()
        createNavController()
        setBottomNavigation()
    }


    private fun createNavController() {
        val navHostFragment = setUpNavHostFragment()
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph_home, intent.extras)
    }

    private fun setContent() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpNavHostFragment() =
        supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

    private fun setBottomNavigation() {
        setSelectedItem()
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeView -> navController.navigate(R.id.homeViewFragment)
            }
            true
        }
    }

    private fun setSelectedItem() {
        binding.bottomNavigationView.selectedItemId = R.id.homeView
    }
}