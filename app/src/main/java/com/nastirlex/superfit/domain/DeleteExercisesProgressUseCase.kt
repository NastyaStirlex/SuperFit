package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class DeleteExercisesProgressUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute() {
        encryptedSharedPreferences.deleteCrunch()
        encryptedSharedPreferences.deletePlank()
        encryptedSharedPreferences.deletePushUps()
        encryptedSharedPreferences.deleteSquats()
        encryptedSharedPreferences.deleteRunning()
    }
}