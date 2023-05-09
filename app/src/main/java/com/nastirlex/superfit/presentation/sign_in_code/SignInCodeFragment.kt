package com.nastirlex.superfit.presentation.sign_in_code

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSignInCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInCodeFragment : Fragment() {
    private lateinit var binding: FragmentSignInCodeBinding

    private val signInCodeViewModel: SignInCodeViewModel by viewModels()

    private val args: SignInCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInCodeBinding.inflate(inflater, container, false)

        setupUsername()

        setupOnBackButtonClick()
        setupOnFirstButtonClick()
        setupOnSecondButtonClick()
        setupOnThirdButtonClick()
        setupOnFourthButtonClick()
        setupOnFifthButtonClick()
        setupOnSixthButtonClick()
        setupOnSeventhButtonClick()
        setupOnEighthButtonClick()
        setupOnNinthButtonClick()

        setupSignInCodeStateObserver()

        return binding.root
    }

    private fun setupUsername() {
        binding.usernameTextView.text = args.username
    }

    private fun setupSignInCodeStateObserver() {
        signInCodeViewModel.signInCodeState.observe(viewLifecycleOwner) {
            when (it) {
                is SignInCodeState.CorrectCodeLength -> {
                    signInCodeViewModel.send(SignIn(args.username, it.code))
                }

                is SignInCodeState.UnsuccessfulSignIn -> {
                    Toast.makeText(requireContext(), R.string.sign_in_failed, Toast.LENGTH_SHORT).show()
                }

                is SignInCodeState.SuccessfulSignIn -> {
                    findNavController().navigate(R.id.main_nav_graph)
                }

                else -> {}
            }
        }
    }

    private fun setupOnBackButtonClick() {
        binding.backImageButton.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
    }

    private fun setupOnFirstButtonClick() {
        binding.firstButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('1'))
        }
    }

    private fun setupOnSecondButtonClick() {
        binding.secondButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('2'))
        }
    }

    private fun setupOnThirdButtonClick() {
        binding.thirdButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('3'))
        }
    }

    private fun setupOnFourthButtonClick() {
        binding.fourthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('4'))
        }
    }

    private fun setupOnFifthButtonClick() {
        binding.fifthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('5'))
        }
    }

    private fun setupOnSixthButtonClick() {
        binding.sixthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('6'))
        }
    }

    private fun setupOnSeventhButtonClick() {
        binding.seventhButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('7'))
        }
    }

    private fun setupOnEighthButtonClick() {
        binding.eighthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('8'))
        }
    }

    private fun setupOnNinthButtonClick() {
        binding.ninthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('9'))
        }
    }

}