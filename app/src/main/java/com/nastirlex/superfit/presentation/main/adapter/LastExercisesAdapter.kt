package com.nastirlex.superfit.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.ItemExercisesListBinding
import com.nastirlex.superfit.net.dto.TrainingDto
import com.nastirlex.superfit.presentation.TrainingType

class LastExercisesAdapter(private val lastExercises: List<TrainingDto>) :
    RecyclerView.Adapter<LastExercisesAdapter.LastExercisesViewHolder>() {
    class LastExercisesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = ItemExercisesListBinding.bind(view)

        fun bind(lastExercise: TrainingDto) {
            when (lastExercise.exercise) {
                "PUSH_UP" -> {
                    viewBinding.exerciseImageView.setImageResource(TrainingType.PushUp.image)
                    viewBinding.exerciseTitleTextView.setText(TrainingType.PushUp.title)
                    viewBinding.exerciseDescriptionTextView.setText(TrainingType.PushUp.description)
                }
                "PLANK" -> {
                    viewBinding.exerciseImageView.setImageResource(TrainingType.Plank.image)
                    viewBinding.exerciseTitleTextView.setText(TrainingType.Plank.title)
                    viewBinding.exerciseDescriptionTextView.setText(TrainingType.Plank.description)
                }
                "SQUATS" -> {
                    viewBinding.exerciseImageView.setImageResource(TrainingType.Squats.image)
                    viewBinding.exerciseTitleTextView.setText(TrainingType.Squats.title)
                    viewBinding.exerciseDescriptionTextView.setText(TrainingType.Squats.description)
                }
                "CRUNCH" -> {
                    viewBinding.exerciseImageView.setImageResource(TrainingType.Crunch.image)
                    viewBinding.exerciseTitleTextView.setText(TrainingType.Crunch.title)
                    viewBinding.exerciseDescriptionTextView.setText(TrainingType.Crunch.description)
                }
                "RUNNING" -> {
                    viewBinding.exerciseImageView.setImageResource(TrainingType.Running.image)
                    viewBinding.exerciseTitleTextView.setText(TrainingType.Running.title)
                    viewBinding.exerciseDescriptionTextView.setText(TrainingType.Running.description)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastExercisesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercises_list, parent, false)

        return LastExercisesViewHolder(view)

    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: LastExercisesViewHolder, position: Int) {
        holder.bind(lastExercises[position])
    }
}