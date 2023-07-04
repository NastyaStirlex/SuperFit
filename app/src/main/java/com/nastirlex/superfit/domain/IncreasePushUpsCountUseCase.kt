package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class IncreasePushUpsCountUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(pushUpsCount: Int) {
        encryptedSharedPreferences.savePushUps(pushUpsCount)
    }
}