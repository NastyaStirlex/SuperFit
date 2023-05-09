package com.nastirlex.superfit.presentation.signup

sealed class SignUpState {
    object SuccessfulSignUp: SignUpState()
    object UnsuccessfulSignUp: SignUpState()
    object EmptyFields: SignUpState()
    object MismatchedPasswords: SignUpState()
    object InvalidEmail: SignUpState()
    object CodeContainsZero: SignUpState()
    object InvalidCodeLength: SignUpState()
}