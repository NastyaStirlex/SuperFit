package com.nastirlex.superfit.presentation.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.nastirlex.superfit.R

class MessageDialogFragment(private val message: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(message))
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .create()


    companion object {
        const val TAG = "MessageDialog"
    }
}