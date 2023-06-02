package com.nastirlex.superfit.net.repository

import com.nastirlex.superfit.net.dto.AccessTokenResponse
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenResponse

interface RefreshTokenRepository {
    suspend fun getRefreshToken(authorizationBody: AuthorizationBodyDto): RefreshTokenResponse

    suspend fun getAccessTokenWithRefreshToken(refreshToken: RefreshTokenBodyDto): AccessTokenResponse
}