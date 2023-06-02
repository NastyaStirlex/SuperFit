package com.nastirlex.superfit.presentation.running

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentRunningBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningFragment : Fragment() {
    lateinit var binding: FragmentRunningBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningBinding.inflate(inflater, container, false)

        return binding.root
    }
}