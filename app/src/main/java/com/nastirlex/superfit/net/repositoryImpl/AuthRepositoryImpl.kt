package com.nastirlex.superfit.net.repositoryImpl

import android.util.Log
import com.nastirlex.superfit.net.dto.RegistrationBodyDto
import com.nastirlex.superfit.net.repository.AuthRepository
import com.nastirlex.superfit.net.service.AuthService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) : AuthRepository {
    override suspend fun register(registrationBody: RegistrationBodyDto) {
        try {
            authService.register(registrationBody)
        } catch (e: Exception) {
            Log.d("register error", e.localizedMessage ?: "null")
        }
    }
}