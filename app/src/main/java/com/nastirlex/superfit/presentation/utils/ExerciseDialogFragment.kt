package com.nastirlex.superfit.presentation.utils

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R

class ExerciseDialogFragment(
    private val message: String,
    private val onPositiveButtonClick: () -> Unit,
    private val onNegativeButtonClick: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.exercise_dialog_title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.exercise_dialog_positive_button)) { _, _ -> onPositiveButtonClick.invoke() }
            .setNegativeButton(
                getString(R.string.exercise_dialog_negative_button)
            ) { _, _ ->
                onNegativeButtonClick.invoke()
            }
            .create()


    companion object {
        const val TAG = "ExerciseDialog"
    }
}