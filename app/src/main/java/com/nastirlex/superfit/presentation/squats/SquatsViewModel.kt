package com.nastirlex.superfit.presentation.squats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.net.dto.TrainingDto
import com.nastirlex.superfit.net.repositoryImpl.TrainingRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SquatsViewModel @Inject constructor(
    private val trainingRepositoryImpl: TrainingRepositoryImpl
) : ViewModel() {

    fun send(squatsEvent: SquatsEvent) {
        when (squatsEvent) {
            is SaveTraining -> {
                saveTraining(squatsEvent.date, squatsEvent.exercise, squatsEvent.repeatCount)
            }
        }
    }

    private fun saveTraining(date: String, exercise: String, repeatCount: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            trainingRepositoryImpl.saveTraining(TrainingDto(date, exercise, repeatCount))
        }
}