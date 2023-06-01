package com.nastirlex.superfit.net.repositoryImpl

import com.nastirlex.superfit.net.dto.TrainingDto
import com.nastirlex.superfit.net.repository.TrainingRepository
import com.nastirlex.superfit.net.service.TrainingService
import retrofit2.Response
import javax.inject.Inject

open class TrainingRepositoryImpl @Inject constructor(private val trainingService: TrainingService) :
    TrainingRepository {
    override suspend fun getTrainings(): List<TrainingDto> {
        return trainingService.getTrainings()
    }

    override suspend fun saveTraining(training: TrainingDto): Response<TrainingDto> {
        return trainingService.saveTraining(training)
    }
}