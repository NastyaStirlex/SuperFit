package com.nastirlex.superfit.presentation.unsuccess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nastirlex.superfit.databinding.FragmentUnsuccessBinding

class UnSuccessFragment : Fragment() {
    lateinit var binding: FragmentUnsuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnsuccessBinding.inflate(inflater, container, false)

        return binding.root
    }
}