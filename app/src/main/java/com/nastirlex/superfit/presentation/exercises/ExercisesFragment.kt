package com.nastirlex.superfit.presentation.exercises

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentExercisesBinding
import com.nastirlex.superfit.presentation.exercises.adapter.ExercisesListAdapter

class ExercisesFragment : Fragment() {
    private lateinit var binding: FragmentExercisesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercisesBinding.inflate(inflater, container, false)

        // Recycler Views
        setupExercisesRecyclerView()

        // Buttons clicks
        setupOnBackButtonClick()

        return binding.root
    }

    private fun setupOnBackButtonClick() {
        binding.backImageButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupExercisesRecyclerView() {
        binding.exercisesRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        binding.exercisesRecyclerView.adapter = ExercisesListAdapter(
            onCrunchClick = { findNavController().navigate(R.id.crunch_nav_graph) },
            onPlankClick = { findNavController().navigate(R.id.plank_nav_graph) },
            onPushUpsClick = { findNavController().navigate(R.id.push_ups_nav_graph) },
            onRunningClick = { findNavController().navigate(R.id.running_nav_graph) },
            onSquatsClick = { findNavController().navigate(R.id.squats_nav_graph) }
        )
    }
}