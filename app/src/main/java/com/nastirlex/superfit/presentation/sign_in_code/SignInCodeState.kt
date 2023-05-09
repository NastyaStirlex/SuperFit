package com.nastirlex.superfit.presentation.sign_in_code

sealed class SignInCodeState {
    class CorrectCodeLength(val code: Int): SignInCodeState()
    object UnsuccessfulSignIn: SignInCodeState()
    object SuccessfulSignIn: SignInCodeState()
}