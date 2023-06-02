package com.nastirlex.superfit.presentation.squats

import com.nastirlex.superfit.presentation.crunch.CrunchState

sealed class SquatsState {
    class SquatsCountFromStorage(val squatsCount: String) : SquatsState()
    object SuccessfulSavingTraining: SquatsState()
    object HttpError : SquatsState()
    object NetworkError : SquatsState()
    object UnknownError : SquatsState()
}