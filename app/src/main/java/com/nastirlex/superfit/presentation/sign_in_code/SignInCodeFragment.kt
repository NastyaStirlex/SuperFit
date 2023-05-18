package com.nastirlex.superfit.presentation.sign_in_code

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

//        var buttonsCoordinates = mutableListOf(
//            Pair(binding.firstButton.x, binding.firstButton.y),
//            Pair(binding.secondButton.x, binding.secondButton.y),
//            Pair(binding.thirdButton.x, binding.thirdButton.y),
//            Pair(binding.fourthButton.x, binding.fourthButton.y),
//            Pair(binding.fifthButton.x, binding.fifthButton.y),
//            Pair(binding.sixthButton.x, binding.sixthButton.y),
//            Pair(binding.seventhButton.x, binding.seventhButton.y),
//            Pair(binding.eighthButton.x, binding.eighthButton.y),
//            Pair(binding.ninthButton.x, binding.ninthButton.y)
//        )

        //Log.d("buttonsCoordinates", buttonsCoordinates[6].toString())

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
        val firstPairCoordinates = IntArray(2)
        binding.firstButton2.getLocationOnScreen(firstPairCoordinates)

        val secondPairCoordinates = IntArray(2)
        binding.secondButton2.getLocationOnScreen(secondPairCoordinates)

        val thirdPairCoordinates = IntArray(2)
        binding.thirdButton2.getLocationOnScreen(thirdPairCoordinates)

        val fourthPairCoordinates = IntArray(2)
        binding.fourthButton2.getLocationOnScreen(fourthPairCoordinates)

        val fifthPairCoordinates = IntArray(2)
        binding.fifthButton2.getLocationOnScreen(fifthPairCoordinates)

        val sixthPairCoordinates = IntArray(2)
        binding.sixthButton2.getLocationOnScreen(sixthPairCoordinates)

        val seventhPairCoordinates = IntArray(2)
        binding.seventhButton2.getLocationOnScreen(seventhPairCoordinates)

        val eighthPairCoordinates = IntArray(2)
        binding.eighthButton2.getLocationOnScreen(eighthPairCoordinates)

        val ninthPairCoordinates = IntArray(2)
        binding.ninthButton2.getLocationOnScreen(ninthPairCoordinates)

        val buttonsCoordinates = mutableListOf(
            Pair(firstPairCoordinates[0], firstPairCoordinates[1]),
            Pair(secondPairCoordinates[0], secondPairCoordinates[1]),
            Pair(thirdPairCoordinates[0], thirdPairCoordinates[1]),
            Pair(fourthPairCoordinates[0], fourthPairCoordinates[1]),
            Pair(fifthPairCoordinates[0], fifthPairCoordinates[1]),
            Pair(sixthPairCoordinates[0], sixthPairCoordinates[1]),
            Pair(seventhPairCoordinates[0], seventhPairCoordinates[1]),
            Pair(eighthPairCoordinates[0], eighthPairCoordinates[1]),
            Pair(ninthPairCoordinates[0], ninthPairCoordinates[1])
        )
        for (i in 0 until buttonsCoordinates.size) {
            Log.d("coord", buttonsCoordinates[i].toString())
        }
        for (i in 0..8) {
            lateinit var view: Button
            when (i) {
                0 -> {
                    view = binding.firstButton2
                }

                1 -> {
                    view = binding.secondButton2
                }

                2 -> {
                    view = binding.thirdButton2
                }

                3 -> {
                    view = binding.fourthButton2
                }

                4 -> {
                    view = binding.fifthButton2
                }

                5 -> {
                    view = binding.sixthButton2
                }

                6 -> {
                    view = binding.seventhButton2
                }

                7 -> {
                    view = binding.eighthButton2
                }

                8 -> {
                    view = binding.ninthButton2
                }
            }
            val randomPosition = Random.nextInt(0, buttonsCoordinates.size)
            val positionX = buttonsCoordinates[randomPosition].first
            val positionY = buttonsCoordinates[randomPosition].second
            buttonsCoordinates.removeAt(randomPosition)


//            val animX: ObjectAnimator =
//                ObjectAnimator.ofInt(view, "translationX", positionX)
//            val animY: ObjectAnimator =
//                ObjectAnimator.ofInt(view, "translationY", positionY)
//
//
//            val animSet = AnimatorSet()
//            animSet.playTogether(
//                animX,
//                animY
//            )
//            animSet.duration = 2000
//            animSet.start()
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
        binding.firstButton2.setOnClickListener {
            signInCodeViewModel.send(ChangePassword('1'))
            shuffleButtons()
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