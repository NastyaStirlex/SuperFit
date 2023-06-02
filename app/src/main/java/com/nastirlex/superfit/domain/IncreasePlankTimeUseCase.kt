package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class IncreasePlankTimeUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(plankTime: Int) {
        encryptedSharedPreferences.savePlank(plankTime)
    }
}