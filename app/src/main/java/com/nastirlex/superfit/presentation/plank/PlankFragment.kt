package com.nastirlex.superfit.presentation.plank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentPlankBinding
import com.nastirlex.superfit.presentation.TrainingType
import com.nastirlex.superfit.presentation.utils.ExerciseDialogFragment
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class PlankFragment : Fragment() {
    lateinit var binding: FragmentPlankBinding

    private val plankViewModel: PlankViewModel by viewModels()

    lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlankBinding.inflate(inflater, container, false)

        setupPlankStateObserver()


        return binding.root
    }

    private fun setupPlankStateObserver() {
        plankViewModel.plankStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is PlankState.SuccessfulSavingTraining -> {
                    val action = PlankFragmentDirections.actionPlankFragmentToSuccessNavGraph(
                        TrainingType.Plank.title
                    )
                    findNavController().navigate(action)
                }

                is PlankState.PlankTimeFromStorage -> {
                    setupDialog(it.plankTime.toInt())
                    setupPlankTime(it.plankTime)
                    setupProgressBar(it.plankTime)
                }

                is PlankState.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is PlankState.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is PlankState.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
            }
        }
    }

    private fun setupDialog(plankTime: Int) {
        ExerciseDialogFragment(
            message = resources.getString(
                R.string.exercise_dialog_message,
                plankTime
            ),
            onPositiveButtonClick = { setupTimer() },
            onNegativeButtonClick = { findNavController().navigateUp() }
        ).show(childFragmentManager, ExerciseDialogFragment.TAG)

    }

    private fun setupTimer() {
        timer = Timer()
        val task: TimerTask = timerTask {
            activity?.runOnUiThread {
                val decreasedPlankTime =
                    binding.plankTimeTextView.text.toString().toInt() - 1
                binding.plankTimeTextView.text = decreasedPlankTime.toString()

                binding.progressBar.progress--

                if (binding.progressBar.progress == 0) {
                    plankViewModel.send(SaveTraining())
                }
            }
        }
        timer.schedule(task, 0, 1000)
    }

    private fun setupPlankTime(plankTime: String) {
        binding.plankTimeTextView.text = plankTime
    }

    private fun setupProgressBar(plankTime: String) {
        binding.progressBar.progress = plankTime.toInt()
        binding.progressBar.max = plankTime.toInt()
    }
}