package com.nastirlex.superfit.presentation.running

sealed class RunningState {
    class RunningMetersFromStorage(val runningMeters: String) : RunningState()
    object SuccessfulSavingTraining: RunningState()
    class FinishTraining(val runningMetersLeft: String) : RunningState()
    object HttpError : RunningState()
    object NetworkError : RunningState()
    object UnknownError : RunningState()
}