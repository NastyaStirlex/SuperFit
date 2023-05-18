package com.nastirlex.superfit.presentation.sign_in_code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.SaveUserInfoUseCase
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SignInCodeViewModel @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val saveUserInfoUseCase: SaveUserInfoUseCase
) : ViewModel() {

    private val _signInCodeState = MutableLiveData<SignInCodeState>()
    val signInCodeState: LiveData<SignInCodeState>
        get() = _signInCodeState

    private val _password = MutableLiveData("")
    val password: LiveData<String>
        get() = _password

    fun send(event: SignInCodeEvent) {
        when (event) {
            is ChangePassword -> {
                changePassword(event.digit)
            }

            is SignIn -> {
                authorization(event.username, event.code)
            }
        }
    }

    private fun changePassword(digit: Char) {
        _password.value = _password.value + digit
        if (_password.value?.length == 4) {
            _signInCodeState.value = SignInCodeState.CorrectCodeLength(_password.value!!.toInt())
            _password.value = ""
        }
    }

    private fun authorization(username: String, code: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val refreshToken = authRepositoryImpl.getRefreshToken(
                    AuthorizationBodyDto(
                        email = username,
                        code = code
                    )
                ).refreshToken

                val accessToken =
                    authRepositoryImpl.getAccessTokenWithRefreshToken(
                        RefreshTokenBodyDto(
                            refreshToken
                        )
                    ).accessToken

                saveUserInfoUseCase.execute(accessToken, username, code.toString())
                _signInCodeState.postValue(SignInCodeState.SuccessfulSignIn)

            } catch (e: Exception) {
                when (e) {
                    is HttpException -> when (e.code()) {
                        404 -> {
                            _signInCodeState.postValue(SignInCodeState.UnsuccessfulSignIn)
                        }
                        else -> {
                            _signInCodeState.postValue(SignInCodeState.HttpError)
                        }
                    }

                    is UnknownHostException, is SocketException -> {
                        _signInCodeState.postValue(SignInCodeState.NetworkError)
                    }

                    else -> {
                        _signInCodeState.postValue(SignInCodeState.UnknownError)
                    }
                }
            }
        }
}