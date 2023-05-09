package com.nastirlex.superfit.net

import android.content.Context
import com.nastirlex.superfit.domain.GetTokenUsecase
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthorizationInterceptor @Inject constructor(private val getTokenUsecase: GetTokenUsecase) :
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