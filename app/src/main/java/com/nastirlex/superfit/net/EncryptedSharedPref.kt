package com.nastirlex.superfit.net

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
val ENCRYPTED_SHARED_PREFERENCES = ""
class EncryptedSharedPref @Inject constructor(@ApplicationContext context: Context) {
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        ENCRYPTED_SHARED_PREFERENCES,
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // ACCESS TOKEN
    fun saveAccessToken(accessToken: String) {
        with(editor) {
            putString("accessToken", accessToken)
            apply()
        }
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString("accessToken", "empty").toString()
    }

    fun deleteAccessToken() {
        with(editor) {
            remove("accessToken")
            apply()
        }
    }

    // USERNAME
    fun saveUsername(username: String) {
        with(editor) {
            putString("username", username)
            apply()
        }
    }

    fun getUsername(): String {
        return sharedPreferences.getString("username", "empty").toString()
    }

    fun deleteUsername() {
        with(editor) {
            remove("username")
            apply()
        }
    }

    // PASSWORD
    fun savePassword(password: String) {
        with(editor) {
            putString("password", password)
            apply()
        }
    }

    fun getPassword(): String {
        return sharedPreferences.getString("password", "empty").toString()
    }

    fun deletePassword() {
        with(editor) {
            remove("password")
            apply()
        }
    }

    // FIRST RUN
    fun saveFirstRun(isFirstRun: Boolean) {
        with(editor) {
            putBoolean("firstRun", isFirstRun)
            apply()
        }
    }

    fun getFirstRun(): Boolean {
        return sharedPreferences.getBoolean("firstRun", true)
    }

}