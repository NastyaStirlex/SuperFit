package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class IncreaseRunningMetersUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(runningMeters: Int) {
        encryptedSharedPreferences.saveRunning(runningMeters)
    }
}