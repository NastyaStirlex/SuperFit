package com.nastirlex.superfit.net.service

import com.nastirlex.superfit.net.dto.RegistrationBodyDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/register")
    suspend fun register(@Body registrationBody: RegistrationBodyDto)
}