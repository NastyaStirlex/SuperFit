package com.nastirlex.superfit.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(authRepositoryImpl: AuthRepositoryImpl) : ViewModel() {

    private val _signInStateLiveMutable = MutableLiveData<SignInState>()
    val signInStateLive: LiveData<SignInState>
        get() = _signInStateLiveMutable

    fun send(event: SignInEvent) {
        when (event) {
            is NavigateToPasswordInputEvent -> {
                checkUsernameEmptyness(event.username)
            }
        }
    }

    private fun checkUsernameEmptyness(username: String) {
        if (username.isBlank()) {
            _signInStateLiveMutable.value = SignInState.EmptyUsername
        } else {
            _signInStateLiveMutable.value = SignInState.Success(username)
        }
    }
}