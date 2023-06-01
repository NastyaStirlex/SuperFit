package com.nastirlex.superfit.presentation.crunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentCrunchBinding
import com.nastirlex.superfit.presentation.TrainingType
import com.nastirlex.superfit.presentation.utils.ExerciseType
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class CrunchFragment : Fragment() {
    lateinit var binding: FragmentCrunchBinding

    private val crunchViewModel: CrunchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrunchBinding.inflate(inflater, container, false)

        setupCrunchStateObserver()

        setupOnFinishButtonClick()

        return binding.root
    }

    private fun setupCrunchStateObserver() {
        crunchViewModel.crunchStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is CrunchState.SuccessfulSavingTraining -> {
                    val action = CrunchFragmentDirections.actionCrunchFragmentToSuccessNavGraph(
                        exerciseName = TrainingType.Crunch.title
                    )
                    findNavController().navigate(action)
                }

                is CrunchState.CrunchCountFromStorage -> {
                    setupExercisesCount(it.crunchCount)
                    setupProgressBar(it.crunchCount)
                }

                is CrunchState.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is CrunchState.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is CrunchState.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
            }
        }
    }

    private fun setupExercisesCount(exercisesCount: String) {
        binding.crunchTimesTextView.text = exercisesCount
    }

    private fun setupProgressBar(exercisesCount: String) {
        binding.progressBar.progress = exercisesCount.toInt()
        binding.progressBar.max = exercisesCount.toInt()
    }


    private fun setupOnFinishButtonClick() {
        binding.finishButton.setOnClickListener {
            crunchViewModel.send(SaveTraining())
        }
    }

}