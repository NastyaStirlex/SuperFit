package com.nastirlex.superfit.net

import com.nastirlex.superfit.domain.GetAccessTokenUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthorizationInterceptor @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        if (request.header("Authorization") == null) {
            builder.addHeader(
                "Authorization",
                "Bearer ${getAccessTokenUseCase.execute()}"
            )
        }

//        val response = chain.proceed(builder.build())
//
//
//        if (response.code == 401) {
//            val refreshToken = encryptedSharedPref.getRefreshToken()
//            val accessToken = runBlocking {
//                getNewAccessTokenUseCase.execute(refreshToken)
//            }
//
//            encryptedSharedPref.saveAccessToken(accessToken)
//
//
//            // encryptedSharedPref.saveIsTokenExpired(true)
//        }
//
//        response.close()

        return chain.proceed(builder.build())
    }
}