package com.nastirlex.superfit.net.service

import com.nastirlex.superfit.net.dto.TrainingDto
import retrofit2.http.GET

interface TrainingService {
    @GET("api/trainings")
    suspend fun getTrainings(): List<TrainingDto>
}