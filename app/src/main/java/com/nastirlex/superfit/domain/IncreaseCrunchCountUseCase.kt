package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class IncreaseCrunchCountUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(crunchCount: Int) {
        encryptedSharedPreferences.saveCrunch(crunchCount)
    }
}