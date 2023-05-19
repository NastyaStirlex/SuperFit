package com.nastirlex.superfit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.nastirlex.superfit.databinding.ActivityMainBinding
import com.nastirlex.superfit.net.EncryptedSharedPref
import com.nastirlex.superfit.net.repositoryImpl.TrainingRepositoryImpl
import com.nastirlex.superfit.net.service.TrainingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

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
        val accessToken = sharedPrefs.getAccessToken()
        val isFirstRun = sharedPrefs.getFirstRun()
        val isTokenExpired = sharedPrefs.getIsTokenExpired()

        Log.d("first run", isFirstRun.toString())
        Log.d("access token", accessToken)
        Log.d("is token expired",isTokenExpired.toString())

        if (isFirstRun) {
            sharedPrefs.saveFirstRun(false)
            navGraph.setStartDestination(R.id.sign_up_nav_graph)
            navController.graph = navGraph
        } else if (sharedPrefs.getAccessToken() != "empty" && sharedPrefs.getIsTokenExpired()) {
            navGraph.setStartDestination(R.id.main_nav_graph)
            navController.graph = navGraph
        } else {
            navGraph.setStartDestination(R.id.sign_in_nav_graph)
            navController.graph = navGraph
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}