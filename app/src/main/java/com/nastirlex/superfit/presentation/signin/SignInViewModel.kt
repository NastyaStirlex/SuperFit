package com.nastirlex.superfit.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nastirlex.superfit.net.EncryptedSharedPref
import com.nastirlex.superfit.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val encryptedSharedPref: EncryptedSharedPref
) :
    ViewModel() {

    private val _signInStateLiveMutable = MutableLiveData<SignInState>()
    val signInStateLive: LiveData<SignInState>
        get() = _signInStateLiveMutable

    init {
        getUsername()
    }

    fun send(event: SignInEvent) {
        when (event) {
            is NavigateToPasswordInputEvent -> {
                checkUsernameEmptiness(event.username)
            }
        }
    }

    private fun checkUsernameEmptiness(username: String) {
        if (username.isBlank()) {
            _signInStateLiveMutable.value = SignInState.EmptyUsername
        } else {
            _signInStateLiveMutable.value = SignInState.Success(username)
        }
    }

    private fun getUsername() {
        val username = encryptedSharedPref.getUsername()
        if (username != Constants.CREDENTIALS_DEFAULT_VALUE)
            _signInStateLiveMutable.value =
                SignInState.UsernameFromStorage(username)
    }
}