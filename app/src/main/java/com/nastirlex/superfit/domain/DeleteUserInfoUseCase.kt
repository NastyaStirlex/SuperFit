package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class DeleteUserInfoUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute() {
        encryptedSharedPreferences.deleteAccessToken()
        encryptedSharedPreferences.deleteUsername()
        encryptedSharedPreferences.deletePassword()
    }
}