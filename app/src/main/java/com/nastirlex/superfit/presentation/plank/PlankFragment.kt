package com.nastirlex.superfit.presentation.plank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentPlankBinding

class PlankFragment : Fragment() {
    lateinit var binding: FragmentPlankBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlankBinding.inflate(inflater, container, false)

        return binding.root
    }
}