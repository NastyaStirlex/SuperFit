package com.nastirlex.superfit.net.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("expired")
    val expired: Int,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("username")
    val username: String
)