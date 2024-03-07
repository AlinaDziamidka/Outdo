package com.example.graduationproject.presentation.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityHomeBinding

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
                R.id.myGroupsView -> navController.navigate()
                R.id.competitionsView -> navController.navigate()
                R.id.friendsView -> navController.navigate()
                R.id.accountView -> navController.navigate()
            }
            true
        }
    }

    private fun setSelectedItem() {
        binding.bottomNavigationView.selectedItemId = R.id.homeView
    }
}