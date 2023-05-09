package com.nastirlex.superfit.presentation.sign_in_code

interface SignInCodeEvent

class ChangePassword(val digit: Char): SignInCodeEvent

class SignIn(val username: String, val code: Int): SignInCodeEvent