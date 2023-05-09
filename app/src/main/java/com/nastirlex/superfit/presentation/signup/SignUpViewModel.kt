package com.nastirlex.superfit.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import com.nastirlex.superfit.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {

    private val _signUpStateLiveMutable = MutableLiveData<SignUpState>()
    val signUpStateLive: LiveData<SignUpState>
        get() = _signUpStateLiveMutable

    init {
        //_signUpStateLiveMutable.value = SignUpState(false)
    }

    fun send(event: SignUpEvent) {
        when (event) {
            is RegistrationEvent -> {
                registration(event.username, event.email, event.code, event.repeatCode)
            }
        }
    }

    private fun registration(username: String, email: String, code: String, repeatCode: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (username.isBlank() || email.isBlank() || code.isBlank() || repeatCode.isBlank()) {
                _signUpStateLiveMutable.postValue(SignUpState.EmptyFields)
            } else if (code != repeatCode) {
                _signUpStateLiveMutable.postValue(SignUpState.MismatchedPasswords)
            } else if (!email.matches(Constants.email_regexp)) {
                _signUpStateLiveMutable.postValue(SignUpState.InvalidEmail)
            } else if (code.contains('0')) {
                _signUpStateLiveMutable.postValue(SignUpState.CodeContainsZero)
            } else if (code.length != 4) {
                _signUpStateLiveMutable.postValue(SignUpState.InvalidCodeLength)
            } else {
                try {
                    authRepositoryImpl.register(
                        AuthorizationBodyDto(
                            email = email, code = code.toInt()
                        )
                    )
                    _signUpStateLiveMutable.postValue(SignUpState.SuccessfulSignUp)
                } catch (e: Exception) {
                    _signUpStateLiveMutable.postValue(SignUpState.UnsuccessfulSignUp)
                }
            }

        }
}