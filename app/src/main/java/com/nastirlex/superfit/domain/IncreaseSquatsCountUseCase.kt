package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class IncreaseSquatsCountUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(squatsCount: Int) {
        encryptedSharedPreferences.saveSquats(squatsCount)
    }
}