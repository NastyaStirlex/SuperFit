package com.nastirlex.superfit.presentation.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R

class MessageDialogFragment(
    private val message: Int,
    private val title: Int = R.string.title_dialog_default,
    private val positiveButton: Int = R.string.positive_button_dialog_default
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(title))
            .setMessage(getString(message))
            .setPositiveButton(getString(positiveButton)) { _, _ -> }
            .create()


    companion object {
        const val TAG = "MessageDialog"
    }
}