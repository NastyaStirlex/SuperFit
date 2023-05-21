package com.nastirlex.superfit.presentation.signin

sealed class SignInState {
    object EmptyUsername: SignInState()
    class Success(val username: String): SignInState()
    class UsernameFromStorage(val username: String): SignInState()
}