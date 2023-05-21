package com.nastirlex.superfit.presentation.exercise

import android.animation.ObjectAnimator
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nastirlex.superfit.databinding.FragmentExerciseBinding
import kotlin.math.abs


class ExerciseFragment : Fragment() {
    lateinit var binding: FragmentExerciseBinding
    lateinit var sensorManager: SensorManager

    private val NOISE = 2.0.toFloat()

    private var mLastX = 0f
    private var mLastY = 0f
    private var mLastZ = 0f

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
                val x = event?.values?.get(0)
                val y = event?.values?.get(1)
                val z = event?.values?.get(2)

                var deltaX: Float = abs(mLastX - x!!)
                var deltaY: Float = abs(mLastY - y!!)
                var deltaZ: Float = abs(mLastZ - z!!)

                if (deltaX < NOISE) deltaX = 0.0.toFloat()
                if (deltaY < NOISE) deltaY = 0.0.toFloat()
                if (deltaZ < NOISE) deltaZ = 0.0.toFloat()

                mLastX = x
                mLastY = y
                mLastZ = z

                binding.xTextView.text = deltaX.toString()
                binding.yTextView.text = deltaY.toString()
                binding.zTextView.text = deltaZ.toString()

                if (deltaX > deltaY) {
                    binding.textView.text = "horizontal"
                } else if (deltaY > deltaX) {
                    binding.textView.text = "vertical"
                } else {
                    binding.textView.text = "nothing"
                }

//                val valuesUpDown = event?.values?.get(0)
//                if (valuesUpDown != null) {
//                    binding.textView4.text = "up/down ${valuesUpDown.toInt()}"
//                }
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