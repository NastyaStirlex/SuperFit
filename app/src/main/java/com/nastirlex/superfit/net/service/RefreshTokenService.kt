package com.nastirlex.superfit.net.service

import com.nastirlex.superfit.net.dto.AccessTokenResponse
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenService {
    @POST("api/auth/token")
    suspend fun getRefreshToken(@Body authorizationBody: AuthorizationBodyDto): RefreshTokenResponse

    @POST("api/auth/token/refresh")
    suspend fun getAccessTokenWithRefreshToken(@Body refreshToken: RefreshTokenBodyDto): AccessTokenResponse
}