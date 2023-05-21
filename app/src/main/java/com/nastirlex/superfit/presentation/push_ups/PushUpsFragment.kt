package com.nastirlex.superfit.presentation.push_ups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentPushUpsBinding

class PushUpsFragment : Fragment() {
    lateinit var binding: FragmentPushUpsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPushUpsBinding.inflate(inflater, container, false)

        return binding.root
    }
}