package com.nastirlex.superfit.net.service

import com.nastirlex.superfit.net.dto.TrainingDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TrainingService {
    @GET("api/trainings")
    suspend fun getTrainings(): List<TrainingDto>

    @POST("api/trainings")
    suspend fun saveTraining(@Body training: TrainingDto): Response<TrainingDto>
}