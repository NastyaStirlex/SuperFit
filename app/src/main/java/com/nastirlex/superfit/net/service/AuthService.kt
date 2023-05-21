package com.nastirlex.superfit.net.service

import com.nastirlex.superfit.net.dto.AccessTokenResponse
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/register")
    suspend fun register(@Body registrationBody: AuthorizationBodyDto)

    @POST("api/auth/token")
    suspend fun getRefreshToken(@Body authorizationBody: AuthorizationBodyDto): RefreshTokenResponse

    @POST("api/auth/token/refresh")
    suspend fun getAccessTokenWithRefreshToken(@Body refreshToken: RefreshTokenBodyDto): AccessTokenResponse

    @GET("api/trainings")
    suspend fun checkTokenExpirationByGetTrainings(@Header("Authorization") token: String): Response<Any>
}