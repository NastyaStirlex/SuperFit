package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.EncryptedSharedPref
import javax.inject.Inject

class SaveUserInfoUseCase @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPref
) {
    fun execute(accessToken: String, refreshToken: String, username: String, password: String) {
        encryptedSharedPreferences.saveAccessToken(accessToken)
        encryptedSharedPreferences.saveRefreshToken(refreshToken)
        encryptedSharedPreferences.saveUsername(username)
        encryptedSharedPreferences.savePassword(password)
    }
}