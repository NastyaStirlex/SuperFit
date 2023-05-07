package com.nastirlex.superfit.presentation.exercises.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.ItemExercisesListBinding

class ExercisesListAdapter :
    RecyclerView.Adapter<ExercisesListAdapter.ExercisesListViewHolder>() {

    private val exercises = arrayOf(
        Exercise(
            R.drawable.push_up,
            R.string.exercise_push_ups,
            R.string.exercise_push_ups_description,
        ),
        Exercise(
            R.drawable.plank,
            R.string.exercise_plank,
            R.string.exercise_plank_description
        ),
        Exercise(
            R.drawable.squats,
            R.string.exercise_squats,
            R.string.exercise_squats_description
        ),
        Exercise(
            R.drawable.crunch,
            R.string.exercise_crunch,
            R.string.exercise_crunch_description
        ),
        Exercise(
            R.drawable.running,
            R.string.exercise_running,
            R.string.exercise_running_description
        )
    )

    class ExercisesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = ItemExercisesListBinding.bind(view)

        fun bind(exercise: Exercise) {
            viewBinding.exerciseImageView.setImageResource(exercise.image)
            viewBinding.exerciseTitleTextView.setText(exercise.title)
            viewBinding.exerciseDescriptionTextView.setText(exercise.description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercises_list, parent, false)

        return ExercisesListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExercisesListViewHolder, position: Int) {
        holder.bind(exercises[position])
    }
}