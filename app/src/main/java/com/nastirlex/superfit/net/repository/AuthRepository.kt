package com.nastirlex.superfit.net.repository

import com.nastirlex.superfit.net.dto.AccessTokenResponse
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun register(registrationBody: AuthorizationBodyDto)
    suspend fun getRefreshToken(authorizationBody: AuthorizationBodyDto): RefreshTokenResponse
    suspend fun getAccessTokenWithRefreshToken(refreshToken: RefreshTokenBodyDto): AccessTokenResponse
    suspend fun checkTokenExpirationByGetTrainings(token: String): Response<Any>
}