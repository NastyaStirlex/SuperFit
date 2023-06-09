package com.nastirlex.superfit.net.repositoryImpl

import com.nastirlex.superfit.net.dto.AccessTokenResponse
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenResponse
import com.nastirlex.superfit.net.repository.RefreshTokenRepository
import com.nastirlex.superfit.net.service.RefreshTokenService
import javax.inject.Inject

class RefreshTokenRepositoryImpl @Inject constructor(
    private val refreshTokenService: RefreshTokenService
) : RefreshTokenRepository {
    override suspend fun getRefreshToken(authorizationBody: AuthorizationBodyDto): RefreshTokenResponse {
        return refreshTokenService.getRefreshToken(authorizationBody)
    }

    override suspend fun getAccessTokenWithRefreshToken(refreshToken: RefreshTokenBodyDto): AccessTokenResponse {
        return refreshTokenService.getAccessTokenWithRefreshToken(refreshToken)
    }
}