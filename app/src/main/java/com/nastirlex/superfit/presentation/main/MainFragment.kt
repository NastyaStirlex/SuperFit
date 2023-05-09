package com.nastirlex.superfit.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentMainBinding
import com.nastirlex.superfit.presentation.main.adapter.LastExercisesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        // Buttons clicks
        setupOnSeeAllButtonClick()
        setupOnSignOutButtonClick()

        // Observers
        setupMainStateObserver()

        return binding.root
    }

    private fun setupMainStateObserver() {
        mainViewModel.mainStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                MainState.SuccessfulSignOut -> {
                    findNavController().navigate(R.id.sign_in_nav_graph)
                }

                MainState.LastExercisesEmpty -> {
                    binding.lastExercisesGroup.visibility = View.INVISIBLE
                }

                else -> {}
            }
        }
    }

    private fun setupOnSeeAllButtonClick() {
        binding.seeAllButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_exercisesFragment)
        }
    }

    private fun setupOnSignOutButtonClick() {
        binding.signOutButton.setOnClickListener {
            mainViewModel.send(SignOut())
        }
    }

    private fun setupLastExercisesRecyclerView() {
        binding.lastExercisesRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        //binding.lastExercisesRecyclerView.adapter = LastExercisesAdapter()
    }

}