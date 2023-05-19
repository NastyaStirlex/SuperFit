package com.nastirlex.superfit.presentation.exercise

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nastirlex.superfit.databinding.FragmentExerciseBinding
import kotlinx.coroutines.delay


class ExerciseFragment : Fragment() {
    lateinit var binding: FragmentExerciseBinding
    var progress = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        //binding.progressBar.show()


        val progressAnim = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 25).apply {
            duration = 6000
        }



        binding.finishButton.setOnClickListener {
//            Toast.makeText(requireContext(), "button clicked", Toast.LENGTH_SHORT).show()
//            progress = binding.progressBar.progress
//            Log.d("progress", progress.toString())
//            progress += 5
//            binding.progressBar.progress = progress
            progressAnim.start()
        }

        return binding.root
    }

}