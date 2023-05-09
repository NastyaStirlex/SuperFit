package com.nastirlex.superfit.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import com.nastirlex.superfit.presentation.sign_in_code.SignInCodeState
import com.nastirlex.superfit.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {

    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState>
        get() = _signUpState

    init {}

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
                _signUpState.postValue(SignUpState.EmptyFields)

            } else if (!email.matches(Constants.email_regexp)) {
                _signUpState.postValue(SignUpState.InvalidEmail)

            } else if (code.contains('0')) {
                _signUpState.postValue(SignUpState.CodeContainsZero)

            } else if (code.length != 4) {
                _signUpState.postValue(SignUpState.InvalidCodeLength)

            } else if (code != repeatCode) {
                _signUpState.postValue(SignUpState.MismatchedPasswords)

            } else {
                try {
                    authRepositoryImpl.register(
                        AuthorizationBodyDto(
                            email = email, code = code.toInt()
                        )
                    )
                    _signUpState.postValue(SignUpState.SuccessfulSignUp)
                } catch (e: Exception) {
                    when (e) {
                        is HttpException -> when (e.code()) {
                            400 -> {
                                _signUpState.postValue(SignUpState.UnsuccessfulSignUp)
                            }
                            else -> {
                                _signUpState.postValue(SignUpState.HttpError)
                            }
                        }

                        is UnknownHostException, is SocketException -> {
                            _signUpState.postValue(SignUpState.NetworkError)
                        }

                        else -> {
                            _signUpState.postValue(SignUpState.UnknownError)
                        }
                    }
                }
            }

        }
}