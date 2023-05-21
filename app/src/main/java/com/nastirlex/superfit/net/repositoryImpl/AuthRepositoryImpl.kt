package com.nastirlex.superfit.net.repositoryImpl

import com.nastirlex.superfit.net.dto.AccessTokenResponse
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenResponse
import com.nastirlex.superfit.net.repository.AuthRepository
import com.nastirlex.superfit.net.service.AuthService
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
    AuthRepository {
    override suspend fun register(registrationBody: AuthorizationBodyDto) =
        authService.register(registrationBody)

    override suspend fun getRefreshToken(authorizationBody: AuthorizationBodyDto): RefreshTokenResponse =
        authService.getRefreshToken(authorizationBody)

    override suspend fun getAccessTokenWithRefreshToken(refreshToken: RefreshTokenBodyDto): AccessTokenResponse =
        authService.getAccessTokenWithRefreshToken(refreshToken)

    override suspend fun checkTokenExpirationByGetTrainings(token: String): Response<Any> {
        return authService.checkTokenExpirationByGetTrainings("Bearer $token")

    }
}