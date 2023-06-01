package com.nastirlex.superfit.net.repository

import com.nastirlex.superfit.net.dto.TrainingDto
import retrofit2.Response

interface TrainingRepository {
    suspend fun getTrainings(): List<TrainingDto>
    suspend fun saveTraining(training: TrainingDto): Response<TrainingDto>
}