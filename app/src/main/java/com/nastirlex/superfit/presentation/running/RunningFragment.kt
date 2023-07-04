package com.nastirlex.superfit.presentation.running

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentRunningBinding
import com.nastirlex.superfit.presentation.TrainingType
import com.nastirlex.superfit.presentation.utils.MessageDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningFragment : Fragment() {
    lateinit var binding: FragmentRunningBinding
    private val runningViewModel: RunningViewModel by viewModels()

    lateinit var sensorManager: SensorManager

    private lateinit var stepSensor: Sensor
    private lateinit var sensorListener: SensorEventListener

    private var running = false

    private var totalSteps = 0

    private var previousTotalSteps = 0f

    var initialStepCount = -1
    var steps = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningBinding.inflate(inflater, container, false)


        onFinishButtonClick()

        setupRunningMetersObserver()

        setupSensorManager()
        setupSensorListener()

        setupPermissionCheck()

        return binding.root
    }

    private fun setupPermissionCheck() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //onStepCounterPermissionGranted()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                RQ_PERMISSION_FOR_STEPCOUNTER_CODE
            )
        }
    }

    private fun setupRunningMetersObserver() {
        runningViewModel.runningStateLiveMutable.observe(viewLifecycleOwner) {
            when (it) {
                is RunningState.SuccessfulSavingTraining -> {
                    val action = RunningFragmentDirections.actionRunningFragmentToSuccessNavGraph(
                        exerciseName = TrainingType.Running.title
                    )
                    findNavController().navigate(action)
                }

                is RunningState.RunningMetersFromStorage -> {
                    setupRunningMeters(it.runningMeters)
                    setupRunningProgressBar(it.runningMeters)
                }

                is RunningState.FinishTraining -> {
                    val action = RunningFragmentDirections.actionRunningFragmentToUnsuccessNavGraph(
                        exerciseName = TrainingType.Running.title,
                        timesLeft = it.runningMetersLeft
                    )
                    findNavController().navigate(action)
                }

                is RunningState.HttpError -> {
                    MessageDialogFragment(R.string.http_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is RunningState.NetworkError -> {
                    MessageDialogFragment(R.string.network_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }

                is RunningState.UnknownError -> {
                    MessageDialogFragment(R.string.unknown_error).show(
                        childFragmentManager,
                        MessageDialogFragment.TAG
                    )
                }
            }
        }
    }

    private fun setupRunningMeters(runningMeters: String) {
        binding.runningTimesTextView.text = runningMeters
    }

    private fun setupRunningProgressBar(runningMeters: String) {
        binding.runningProgressBar.max = runningMeters.toInt() * 100
        binding.runningProgressBar.progress = binding.runningProgressBar.max

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RQ_PERMISSION_FOR_STEPCOUNTER_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //onStepCounterPermissionGranted()
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)) {
                        askUserForOpeningUpSettings()
                    }
                }
            }
        }
    }

    private fun askUserForOpeningUpSettings() {
        val sppSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", "com.nastirlex.superfit.presentation.running", null)
        )
        if (requireContext().packageManager.resolveActivity(
                sppSettingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            MessageDialogFragment(R.string.deny_forever).show(
                childFragmentManager,
                MessageDialogFragment.TAG
            )
        } else {
            MessageDialogFragment(
                message = R.string.deny_permission,
                title = R.string.title_dialog,
                positiveButton = R.string.open_positive_button
            ).show(
                childFragmentManager,
                MessageDialogFragment.TAG
            )
        }
    }

    private fun onStepCounterPermissionGranted() {
        MessageDialogFragment(R.string.permission_granted).show(
            childFragmentManager,
            MessageDialogFragment.TAG
        )
    }

    private companion object {
        const val RQ_PERMISSION_FOR_STEPCOUNTER_CODE = 1
    }

    override fun onResume() {
        super.onResume()
        running = true

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        sensorManager.registerListener(
            sensorListener,
            stepSensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private fun setupSensorManager() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun setupSensorListener() {
        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val totalSteps = event.values[0]
                if (previousTotalSteps == 0f) previousTotalSteps = totalSteps

                val currentSteps = totalSteps.toInt() - previousTotalSteps
                previousTotalSteps = totalSteps

                binding.runningTimesTextView.text =
                    (binding.runningTimesTextView.text.toString().toFloat() - currentSteps).toInt()
                        .toString()

                binding.runningProgressBar.progress = binding.runningProgressBar.progress - 1 * 100

                if (binding.runningTimesTextView.text.toString().toInt() == 0) {
                    runningViewModel.send(SaveTraining())
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
        running = false
    }

    private fun onFinishButtonClick() {
        binding.finishButton.setOnClickListener {
            runningViewModel.send(
                FinishTraining(
                    binding.runningTimesTextView.text.toString().toInt()
                )
            )
        }
    }

}