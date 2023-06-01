package com.nastirlex.superfit.net

import com.nastirlex.superfit.domain.GetAccessTokenUseCase
import com.nastirlex.superfit.domain.GetNewAccessTokenUseCase
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import com.nastirlex.superfit.net.repositoryImpl.RefreshTokenRepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class RefreshAuthenticator @Inject constructor(
    private val encryptedSharedPref: EncryptedSharedPref,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val refreshTokenRepositoryImpl: RefreshTokenRepositoryImpl
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            val refreshToken = encryptedSharedPref.getRefreshToken()

            val newAccessToken = runBlocking {
                refreshTokenRepositoryImpl.getAccessTokenWithRefreshToken(RefreshTokenBodyDto(refreshToken)).accessToken
            }

            encryptedSharedPref.saveAccessToken(newAccessToken)

        } catch (e: Exception) {
            return response.request.newBuilder()
                .header("Authorization", "Bearer ${getAccessTokenUseCase.execute()}")
                .build()
        }

        return if (response.responseCount >= 2) {
            null
        } else {
            response.request.newBuilder()
                .header("Authorization", "Bearer ${getAccessTokenUseCase.execute()}")
                .build()
        }
    }

}

private val Response.responseCount: Int
    get() = generateSequence(this) { it.priorResponse }.count()