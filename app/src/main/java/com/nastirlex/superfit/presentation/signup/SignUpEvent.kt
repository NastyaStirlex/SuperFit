package com.nastirlex.superfit.presentation.signup

interface SignUpEvent

class RegistrationEvent(
    val username: String,
    val email: String,
    val code: String,
    val repeatCode: String
) : SignUpEvent