package com.nastirlex.superfit.net.dto

import com.google.gson.annotations.SerializedName

data class RegistrationBodyDto(
    @SerializedName("login")
    val email: String,

    @SerializedName("password")
    val code: Int
)
