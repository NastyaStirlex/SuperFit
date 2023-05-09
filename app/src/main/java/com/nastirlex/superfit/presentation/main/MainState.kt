package com.nastirlex.superfit.presentation.main

import com.nastirlex.superfit.net.dto.TrainingDto

sealed class MainState {
    object SuccessfulSignOut: MainState()
    class LastExercisesLoaded(val lastExercises: List<TrainingDto>): MainState()
    object LastExercisesEmpty: MainState()
}