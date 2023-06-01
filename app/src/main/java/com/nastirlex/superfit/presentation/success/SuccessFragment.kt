package com.nastirlex.superfit.presentation.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment() {
    lateinit var binding: FragmentSuccessBinding
    private val args: SuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setupExerciseName() {
        binding.textView.setText(args.exerciseName)
    }
}