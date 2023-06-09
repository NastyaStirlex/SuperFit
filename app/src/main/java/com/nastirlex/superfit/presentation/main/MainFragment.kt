package com.nastirlex.superfit.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentMainBinding
import com.nastirlex.superfit.net.dto.TrainingDto
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
        setupOnDetailsButtonClick()

        // Observers
        setupMainStateObserver()

        return binding.root
    }

    private fun setupMainStateObserver() {
        mainViewModel.mainStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.SuccessfulSignOut -> {
                    findNavController().navigate(R.id.sign_in_nav_graph)
                }

                is MainState.LastExercisesEmpty -> {
                    setupNoExercisesImage()
                }

                is MainState.LastExercisesLoaded -> {
                    setupLastExercisesRecyclerView(
                        it.lastExercises
                    )
                }
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

    private fun setupOnDetailsButtonClick() {
        binding.myBodyDetailsButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToMyBodyNavGraph()
            findNavController().navigate(action)
        }
    }

    private fun setupLastExercisesRecyclerView(lastExercises: List<TrainingDto>) {
        binding.lastExercisesRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        binding.lastExercisesRecyclerView.adapter = LastExercisesAdapter(lastExercises)

        binding.lastExercisesRecyclerView.visibility = View.VISIBLE
        binding.noLastExercisesImageView.visibility = View.GONE
    }

    private fun setupNoExercisesImage() {
        binding.lastExercisesRecyclerView.visibility = View.GONE
        binding.noLastExercisesImageView.visibility = View.VISIBLE
    }

}