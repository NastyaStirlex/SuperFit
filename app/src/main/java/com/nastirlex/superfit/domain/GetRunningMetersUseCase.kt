package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class GetRunningMetersUseCase  @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(): Int {
        return encryptedSharedPreferences.getRunning()
    }
}