package com.nastirlex.superfit.presentation.exercise

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.telephony.TelephonyCallback.ServiceStateListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.nastirlex.superfit.databinding.FragmentExerciseBinding
import kotlinx.coroutines.delay


class ExerciseFragment : Fragment() {
    lateinit var binding: FragmentExerciseBinding
    lateinit var sensorManager: SensorManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        //binding.progressBar.show()


        val progressAnim = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 25).apply {
            duration = 6000
        }

        sensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val valuesUpDown = event?.values?.get(1)
                if (valuesUpDown != null) {
                    binding.textView4.text = "up/down ${valuesUpDown.toInt()}"
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }

        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        binding.finishButton.setOnClickListener {
//            Toast.makeText(requireContext(), "button clicked", Toast.LENGTH_SHORT).show()
//            progress = binding.progressBar.progress
//            Log.d("progress", progress.toString())
//            progress += 5
//            binding.progressBar.progress = progress
            progressAnim.start()
        }

        return binding.root
    }

}