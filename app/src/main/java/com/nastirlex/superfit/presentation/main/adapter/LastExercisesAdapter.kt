package com.nastirlex.superfit.presentation.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nastirlex.superfit.databinding.ItemExercisesListBinding

class LastExercisesAdapter: RecyclerView.Adapter<LastExercisesAdapter.LastExercisesViewHolder>() {
    class LastExercisesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val viewBinding = ItemExercisesListBinding.bind(view)

        fun bind() {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastExercisesViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LastExercisesViewHolder, position: Int) {
        holder.bind()
    }
}