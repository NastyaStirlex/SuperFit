package com.nastirlex.superfit.presentation.signin

interface SignInEvent

class NavigateToPasswordInputEvent(val username: String) : SignInEvent

class GetUsername: SignInEvent