package com.example.graduationproject.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
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
        setLoadedStatus()
    }


    private fun createNavController() {
        val navHostFragment = setUpNavHostFragment()
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph_home)
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

    private fun setLoadedStatus() {
        val loadedStatus = intent.getBooleanExtra("loadedStatus", false)
        Log.d("HomeView", "Loaded status from intent: $loadedStatus")
        val args = HomeViewFragmentArgs.Builder(loadedStatus).build().toBundle()
        navController.navigate(R.id.homeViewFragment, args)
    }
}