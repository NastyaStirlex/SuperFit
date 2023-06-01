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
import android.widget.Toast
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.FragmentSquatsBinding
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask

class SquatsFragment : Fragment() {
    lateinit var binding: FragmentSquatsBinding

    var builder = StringBuilder()

    var history = FloatArray(2)
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

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

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
                showInfo()
            }
        }
        timer.schedule(task, 0, 1000)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
        timer.cancel()
    }

    private fun showInfo() {
        val x = valuesAccel[0]
        val y = valuesAccel[1]

        val yChange = history[1] - y

        history[0] = x
        history[1] = y

        if (yChange > 2) { // движение вниз
            direction[1] = "DOWN"
            if (counter == 0) {
                counter++
                Toast.makeText(requireContext(), "вниз с увеличением счетчика, $counter", Toast.LENGTH_SHORT)
                    .show()
            }

            Toast.makeText(requireContext(), "просто вниз, $counter", Toast.LENGTH_SHORT)
                .show()

        } else if (yChange < -2) {
            direction[1] = "UP"
            if (counter == 1) {
                counter--
                binding.progressBar.progress--
                Toast.makeText(requireContext(), "minus exercise, $counter", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Toast.makeText(requireContext(), "просто вверх, $counter", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            direction[1] = "NOTHING"
        }

        builder.setLength(0)
        builder.append(" y: ")
        builder.append(direction[1])

        binding.textView.text = builder.toString()

    }

}