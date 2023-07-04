package com.nastirlex.superfit.presentation.squats

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSquatsBinding
import com.nastirlex.superfit.presentation.TrainingType
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class SquatsFragment : Fragment() {
    lateinit var binding: FragmentSquatsBinding

    private val squatsViewModel: SquatsViewModel by viewModels()

    var history = FloatArray(3)
    var direction = arrayOf("NONE", "NONE")

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
        binding = FragmentSquatsBinding.inflate(inflater, container, false)

        setupSensorManager()
        setupSensorListener()
        setupOnBackButtonClick()

        setupSquatsStateObserver()

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

    private fun setupSquatsStateObserver() {
        squatsViewModel.squatsStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is SquatsState.SuccessfulSavingTraining -> {
                    val action = SquatsFragmentDirections.actionSquatsFragmentToSuccessNavGraph(
                        TrainingType.Squats.title
                    )
                    findNavController().navigate(action)
                }

                is SquatsState.SquatsCountFromStorage -> {
                    setupSquatsCount(it.squatsCount)
                    setupProgressBar(it.squatsCount)
                }

                is SquatsState.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SquatsState.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is SquatsState.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
            }
        }
    }

    private fun setupSquatsCount(squatsCount: String) {
        binding.squatsTimesTextView.text = squatsCount
    }

    private fun setupProgressBar(squatsCount: String) {
        binding.progressBar.progress = squatsCount.toInt()
        binding.progressBar.max = squatsCount.toInt()
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

        val xChange = history[0] - x

        val yChange = history[1] - y

        val zChange = history[2] - z

        history[0] = x
        history[1] = y
        history[2] = z

        if (zChange > 4) { // движение вниз
            direction[1] = "DOWN"
            if (counter == 0) {
                counter++
            }

        } else if (zChange < -4) {
            direction[1] = "UP"
            if (counter == 1) {
                val decreasedSquatsCount =
                    binding.squatsTimesTextView.text.toString().toInt() - 1
                binding.squatsTimesTextView.text = decreasedSquatsCount.toString()

                binding.progressBar.progress--

                if (binding.progressBar.progress == 0) {
                    squatsViewModel.send(SaveTraining())
                }
            }
        }

    }

    private fun setupOnBackButtonClick() {
        binding.backImageButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}