package com.nastirlex.superfit.net

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthorizationInterceptor @Inject constructor(@ApplicationContext context: Context) :
    Interceptor {

    //private val jwtRepositoryImpl by lazy { JwtRepositoryImpl() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        if (request.header("Authorization") == null) {
//            builder.addHeader(
//                "Authorization",
//                "Bearer ${jwtRepositoryImpl.getAccessToken(context)}"
//            )
        }

        return chain.proceed(builder.build())
    }
}