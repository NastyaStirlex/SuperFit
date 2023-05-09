package com.nastirlex.superfit.net

import com.nastirlex.superfit.domain.GetTokenUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthorizationInterceptor @Inject constructor(private val getTokenUsecase: GetTokenUseCase) :
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

        return chain.proceed(builder.build())
    }
}