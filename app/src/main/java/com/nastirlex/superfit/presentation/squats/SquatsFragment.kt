package com.nastirlex.superfit.presentation.squats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSquatsBinding

class SquatsFragment : Fragment() {
    lateinit var binding: FragmentSquatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSquatsBinding.inflate(inflater, container, false)

        return binding.root
    }

}