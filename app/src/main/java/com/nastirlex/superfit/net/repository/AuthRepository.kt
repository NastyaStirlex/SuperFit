package com.nastirlex.superfit.net.repository

import com.nastirlex.superfit.net.dto.RegistrationBodyDto

interface AuthRepository {
    suspend fun register(registrationBody: RegistrationBodyDto)
}