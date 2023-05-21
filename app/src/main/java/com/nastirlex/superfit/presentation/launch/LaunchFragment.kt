package com.nastirlex.superfit.presentation.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentLaunchBinding
import com.nastirlex.superfit.presentation.signin.SignInFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaunchFragment : Fragment() {
    lateinit var binding: FragmentLaunchBinding
    private val launchViewModel: LaunchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLaunchBinding.inflate(inflater, container, false)

        setupObserveLaunchState()

        return binding.root
    }

    private fun setupObserveLaunchState() {
        launchViewModel.launchStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is LaunchState.NavigateToSignIn -> {
                    findNavController().navigate(R.id.sign_in_nav_graph)
                }

                is LaunchState.NavigateToSignUp -> {
                    findNavController().navigate(R.id.sign_up_nav_graph)
                }
            }
        }
    }
}