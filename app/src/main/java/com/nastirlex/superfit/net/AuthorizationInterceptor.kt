package com.nastirlex.superfit.net

import android.util.Log
import com.nastirlex.superfit.domain.GetTokenUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthorizationInterceptor @Inject constructor(
    private val getTokenUsecase: GetTokenUseCase,
    private val encryptedSharedPref: EncryptedSharedPref
) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        if (request.header("Authorization") == null) {
            builder.addHeader(
                "Authorization",
                "Bearer ${getTokenUsecase.execute()}"
            )
        }

        val response = chain.proceed(builder.build())

        when (response.code) {
            401 -> {
                encryptedSharedPref.saveIsTokenExpired(true)
            }
            200 -> {
                encryptedSharedPref.saveIsTokenExpired(false)
            }
        }

        return response
    }
}