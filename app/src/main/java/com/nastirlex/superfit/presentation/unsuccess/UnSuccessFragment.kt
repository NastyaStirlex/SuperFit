package com.nastirlex.superfit.presentation.unsuccess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentUnsuccessBinding

class UnSuccessFragment : Fragment() {
    lateinit var binding: FragmentUnsuccessBinding
    private val args: UnSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnsuccessBinding.inflate(inflater, container, false)

        setupExerciseName()
        setupExerciseTimesLeft()

        setupOnGoHomeButtonClick()

        return binding.root
    }

    private fun setupExerciseName() {
        binding.unSuccessExerciseTitleTextView.setText(args.exerciseName)
    }

    private fun setupExerciseTimesLeft() {
        binding.unSuccessTimesLeftTextView.text = resources.getString(
            R.string.un_success_times_left,
            args.timesLeft.toInt()
        )
    }

    private fun setupOnGoHomeButtonClick() {
        binding.goHomeButton.setOnClickListener {
            findNavController().navigate(R.id.main_nav_graph)
        }
    }
}