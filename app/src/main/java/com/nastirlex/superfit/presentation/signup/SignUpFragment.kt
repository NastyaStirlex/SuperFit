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
        signUpViewModel.signUpStateLive.observe(viewLifecycleOwner) {
            when (it) {
                is SignUpState.SuccessfulSignUp -> {
                    Toast.makeText(requireContext(), "Successful sign up", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.main_nav_graph)
                }

                is SignUpState.UnsuccessfulSignUp -> {
                    Toast.makeText(requireContext(), "Unsuccessful sign up", Toast.LENGTH_SHORT)
                        .show()
                }

                is SignUpState.EmptyFields -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.validation_error_empty_fields,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignUpState.MismatchedPasswords -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.validation_error_mismatched_passwords,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignUpState.InvalidEmail -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.validation_error_invalid_email,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignUpState.CodeContainsZero -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.validation_error_code_contains_zero,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignUpState.InvalidCodeLength -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.validation_error_invalid_code_length,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupOnBackSignInButtonClick() {
        binding.signUpSignInButton.setOnClickListener {
            findNavController().navigateUp()
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