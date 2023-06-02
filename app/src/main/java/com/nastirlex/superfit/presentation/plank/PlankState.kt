package com.nastirlex.superfit.presentation.plank

sealed class PlankState {
    class PlankTimeFromStorage(val plankTime: String) : PlankState()
    object SuccessfulSavingTraining: PlankState()
    object HttpError : PlankState()
    object NetworkError : PlankState()
    object UnknownError : PlankState()
}