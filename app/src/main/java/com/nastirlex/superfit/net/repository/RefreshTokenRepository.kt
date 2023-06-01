package com.nastirlex.superfit.net.repository

import com.nastirlex.superfit.net.dto.AccessTokenResponse
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto

interface RefreshTokenRepository {
    suspend fun getAccessTokenWithRefreshToken(refreshToken: RefreshTokenBodyDto): AccessTokenResponse
}