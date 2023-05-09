package com.nastirlex.superfit.net.repository

import com.nastirlex.superfit.net.dto.TrainingDto

interface TrainingRepository {
    suspend fun getTrainings(): List<TrainingDto>
}