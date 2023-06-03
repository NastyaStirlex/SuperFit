package com.nastirlex.superfit.presentation.sign_in_code

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSignInCodeBinding
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


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

    private fun shuffleButtons() {

        val buttonsCoordinates = mutableListOf(
            Pair(binding.firstButton.x, binding.firstButton.y),
            Pair(binding.secondButton.x, binding.secondButton.y),
            Pair(binding.thirdButton.x, binding.thirdButton.y),
            Pair(binding.fourthButton.x, binding.fourthButton.y),
            Pair(binding.fifthButton.x, binding.fifthButton.y),
            Pair(binding.sixthButton.x, binding.sixthButton.y),
            Pair(binding.seventhButton.x, binding.seventhButton.y),
            Pair(binding.eighthButton.x, binding.eighthButton.y),
            Pair(binding.ninthButton.x, binding.ninthButton.y)
        )

        for (i in 0..8) {
            lateinit var view: Button
            when (i) {
                0 -> {
                    view = binding.firstButton
                }

                1 -> {
                    view = binding.secondButton
                }

                2 -> {
                    view = binding.thirdButton
                }

                3 -> {
                    view = binding.fourthButton
                }

                4 -> {
                    view = binding.fifthButton
                }

                5 -> {
                    view = binding.sixthButton
                }

                6 -> {
                    view = binding.seventhButton
                }

                7 -> {
                    view = binding.eighthButton
                }

                8 -> {
                    view = binding.ninthButton
                }
            }

            val randomPosition = Random.nextInt(0, buttonsCoordinates.size)

            val xEnd = buttonsCoordinates[randomPosition].first
            val yEnd = buttonsCoordinates[randomPosition].second

            val deltaX = xEnd - view.left
            val deltaY = yEnd - view.top

            buttonsCoordinates.removeAt(randomPosition)

            val animX: ObjectAnimator =
                ObjectAnimator.ofFloat(view, "translationX", deltaX)
            val animY: ObjectAnimator =
                ObjectAnimator.ofFloat(view, "translationY", deltaY)


            val animSet = AnimatorSet()
            animSet.playTogether(
                animX,
                animY
            )
            animSet.duration = 500
            animSet.start()
        }
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
                    Toast.makeText(requireContext(), R.string.sign_in_failed, Toast.LENGTH_SHORT)
                        .show()
                }

                is SignInCodeState.SuccessfulSignIn -> {
                    findNavController().navigate(R.id.main_nav_graph)
                }

                is SignInCodeState.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignInCodeState.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SignInCodeState.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
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
            shuffleButtons()
        }
    }

    private fun setupOnSecondButtonClick() {
        binding.secondButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('2'))
            shuffleButtons()
        }
    }

    private fun setupOnThirdButtonClick() {
        binding.thirdButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('3'))
            shuffleButtons()
        }
    }

    private fun setupOnFourthButtonClick() {
        binding.fourthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('4'))
            shuffleButtons()
        }
    }

    private fun setupOnFifthButtonClick() {
        binding.fifthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('5'))
            shuffleButtons()
        }
    }

    private fun setupOnSixthButtonClick() {
        binding.sixthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('6'))
            shuffleButtons()
        }
    }

    private fun setupOnSeventhButtonClick() {
        binding.seventhButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('7'))
            shuffleButtons()
        }
    }

    private fun setupOnEighthButtonClick() {
        binding.eighthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('8'))
            shuffleButtons()
        }
    }

    private fun setupOnNinthButtonClick() {
        binding.ninthButton.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('9'))
            shuffleButtons()
        }
    }

}