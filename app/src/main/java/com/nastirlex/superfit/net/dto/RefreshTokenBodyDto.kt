package com.nastirlex.superfit.net.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenBodyDto(
    @SerializedName("refresh_token")
    val refreshToken: String
)