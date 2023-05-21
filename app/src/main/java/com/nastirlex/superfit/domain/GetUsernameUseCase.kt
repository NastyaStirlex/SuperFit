package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(): String {
        return encryptedSharedPreferences.getUsername()
    }
}