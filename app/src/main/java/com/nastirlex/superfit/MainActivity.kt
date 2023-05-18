package com.nastirlex.superfit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nastirlex.superfit.databinding.ActivityMainBinding
import com.nastirlex.superfit.net.EncryptedSharedPref
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.app_nav_graph)

        val sharedPrefs = EncryptedSharedPref(applicationContext)
        val isFirstRun = sharedPrefs.getFirstRun()

        if (isFirstRun) {
            sharedPrefs.saveFirstRun(false)
            navGraph.setStartDestination(R.id.sign_up_nav_graph)
            navController.graph = navGraph
        } else if (sharedPrefs.getAccessToken() != "empty") {
            navGraph.setStartDestination(R.id.main_nav_graph)
            navController.graph = navGraph
        } else {
            navGraph.setStartDestination(R.id.sign_in_nav_graph)
            navController.graph = navGraph
        }

    }

}