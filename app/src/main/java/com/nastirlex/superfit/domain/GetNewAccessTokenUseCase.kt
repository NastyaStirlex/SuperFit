package com.nastirlex.superfit.domain

import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import javax.inject.Inject

class GetNewAccessTokenUseCase @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
) {
    suspend fun execute(refreshToken: String): String {
        return authRepositoryImpl.getAccessTokenWithRefreshToken(RefreshTokenBodyDto(refreshToken)).accessToken
    }
}