package com.nastirlex.superfit.presentation.crunch

sealed class CrunchState {
    object SuccessfulSavingTraining : CrunchState()
    class CrunchCountFromStorage(val crunchCount: String) : CrunchState()
    object HttpError : CrunchState()
    object NetworkError : CrunchState()
    object UnknownError : CrunchState()
}