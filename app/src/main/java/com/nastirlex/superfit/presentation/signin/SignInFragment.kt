package com.nastirlex.superfit.presentation.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSignInBinding
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        setupSignInStateObserver()

        setupOnSignInPasswordButtonClick()
        setupOnSignUpButtonClick()

        return binding.root
    }

    private fun setupSignInStateObserver() {
        signInViewModel.signInStateLive.observe(viewLifecycleOwner) {
            when (it) {
                is SignInState.EmptyUsername -> {
                    MessageDialogFragment(R.string.validation_empty_username).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignInState.Success -> {
                    val action = SignInFragmentDirections.actionSignInFragmentToSignInCodeFragment(
                        username = it.username
                    )
                    findNavController().navigate(action)
                }

                is SignInState.UsernameFromStorage -> {
                    setupUsername(it.username)
                }
            }
        }
    }

    private fun setupUsername(username: String) {
        binding.signInUsernameEditText.setText(username)
    }

    private fun setupOnSignInPasswordButtonClick() {
        binding.signInSignInButton.setOnClickListener {
            signInViewModel.send(NavigateToPasswordInputEvent(binding.signInUsernameEditText.text.toString()))
        }
    }

    private fun setupOnSignUpButtonClick() {
        binding.signInSignUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_sign_up_nav_graph)
        }
    }

}