package com.nastirlex.superfit.net.dto

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("expired")
    val expired: Int
)