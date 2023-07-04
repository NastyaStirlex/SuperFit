package com.nastirlex.superfit.presentation.push_ups

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentPushUpsBinding
import com.nastirlex.superfit.presentation.TrainingType
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class PushUpsFragment : Fragment() {
    lateinit var binding: FragmentPushUpsBinding
    private val pushUpsViewModel: PushUpsViewModel by viewModels()

    var history = FloatArray(3)

    lateinit var sensorManager: SensorManager

    lateinit var sensorAccel: Sensor

    var valuesAccel = FloatArray(3)

    private lateinit var sensorListener: SensorEventListener
    lateinit var timer: Timer

    private var counter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPushUpsBinding.inflate(inflater, container, false)

        setupSensorManager()
        setupSensorListener()

        setupPushUpsStateObserver()

        setupOnFinishButtonClick()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            sensorListener,
            sensorAccel,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        timer = Timer()
        val task: TimerTask = timerTask {
            activity?.runOnUiThread {
                makeExercise()
            }
        }
        timer.schedule(task, 0, 850)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
        timer.cancel()
    }

    private fun setupPushUpsStateObserver() {
        pushUpsViewModel.pushUpsStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is PushUpsState.SuccessfulSavingTraining -> {
                    val action = PushUpsFragmentDirections.actionPushUpsFragmentToSuccessNavGraph(
                        exerciseName = TrainingType.PushUp.title
                    )
                    findNavController().navigate(action)
                }

                is PushUpsState.PushUpsCountFromStorage -> {
                    setupPushUpsCount(it.pushUpsCount)
                    setupPushUpsProgressBar(it.pushUpsCount)
                }

                is PushUpsState.FinishTraining -> {
                    val action = PushUpsFragmentDirections.actionPushUpsFragmentToUnsuccessNavGraph(
                        exerciseName = TrainingType.PushUp.title,
                        timesLeft = it.pushUpsLeft
                    )
                    findNavController().navigate(action)
                }

                is PushUpsState.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is PushUpsState.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is PushUpsState.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
            }
        }
    }

    private fun setupPushUpsCount(pushUps: String) {
        binding.pushUpsTimesTextView.text = pushUps
    }

    private fun setupPushUpsProgressBar(pushUps: String) {
        binding.pushUpsProgressBar.progress = pushUps.toInt()
        binding.pushUpsProgressBar.max = pushUps.toInt()
    }

    private fun setupSensorManager() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun setupSensorListener() {
        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    for (i in 0..2) {
                        valuesAccel[i] = event.values[i]
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    private fun makeExercise() {
        val x = valuesAccel[0]
        val y = valuesAccel[1]
        val z = valuesAccel[2]

        val yChange = history[1] - y

        history[0] = x
        history[1] = y
        history[2] = z

        if (yChange > 2) {
            if (counter == 0) {
                counter++
            }

        } else if (yChange < -2) {
            if (counter == 1) {
                val decreasedSquatsCount =
                    binding.pushUpsTimesTextView.text.toString().toInt() - 1
                binding.pushUpsTimesTextView.text = decreasedSquatsCount.toString()

                binding.pushUpsProgressBar.progress--

                if (binding.pushUpsProgressBar.progress == 0) {
                    pushUpsViewModel.send(SaveTraining())
                }
            }
        }
    }

    private fun setupOnFinishButtonClick() {
        binding.finishButton.setOnClickListener {
            pushUpsViewModel.send(FinishTraining(binding.pushUpsTimesTextView.text.toString().toInt()))
        }
    }
}