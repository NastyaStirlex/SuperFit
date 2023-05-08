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
            R.string.push_ups_title,
            R.string.push_ups_description,
        ),
        Exercise(
            R.drawable.plank,
            R.string.plank_title,
            R.string.plank_description
        ),
        Exercise(
            R.drawable.squats,
            R.string.squats_title,
            R.string.squats_description
        ),
        Exercise(
            R.drawable.crunch,
            R.string.crunch_title,
            R.string.crunch_description
        ),
        Exercise(
            R.drawable.running,
            R.string.running_title,
            R.string.running_description
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