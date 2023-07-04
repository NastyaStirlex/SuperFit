package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class GetPushUpsCountUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(): Int {
        return encryptedSharedPreferences.getPushUps()
    }
}