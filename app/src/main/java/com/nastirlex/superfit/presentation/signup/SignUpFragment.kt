package com.nastirlex.superfit.presentation.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSignUpBinding
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        setupSignUpStateObserver()
        setupOnBackSignInButtonClick()
        setupOnSignUpButtonClick()

        return binding.root
    }

    private fun setupSignUpStateObserver() {
        signUpViewModel.signUpState.observe(viewLifecycleOwner) {
            when (it) {
                is SignUpState.SuccessfulSignUp -> {
                    findNavController().navigate(R.id.main_nav_graph)
                }

                is SignUpState.UnsuccessfulSignUp -> {
                    MessageDialogFragment(R.string.duplicate_user).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.EmptyFields -> {
                    MessageDialogFragment(R.string.validation_error_empty_fields).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.MismatchedPasswords -> {
                    MessageDialogFragment(R.string.validation_error_mismatched_passwords).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.InvalidEmail -> {
                    MessageDialogFragment(R.string.validation_error_invalid_email).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.CodeContainsZero -> {
                    MessageDialogFragment(R.string.validation_error_code_contains_zero).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.InvalidCodeLength -> {
                    MessageDialogFragment(R.string.validation_error_invalid_code_length).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignUpState.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
            }
        }
    }

    private fun setupOnBackSignInButtonClick() {
        binding.signUpSignInButton.setOnClickListener {
            findNavController().navigate(R.id.sign_in_nav_graph)
        }
    }

    private fun setupOnSignUpButtonClick() {
        binding.signUpSignUpButton.setOnClickListener {
            signUpViewModel.send(
                RegistrationEvent(
                    binding.signUpUsernameEditText.text.toString(),
                    binding.signUpEmailEditText.text.toString(),
                    binding.signUpCodeEditText.text.toString(),
                    binding.signUpRepeatCodeEditText.text.toString()
                )
            )
        }
    }
}