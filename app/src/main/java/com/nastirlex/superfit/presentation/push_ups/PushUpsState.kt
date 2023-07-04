package com.nastirlex.superfit.presentation.push_ups

sealed class PushUpsState {
    class PushUpsCountFromStorage(val pushUpsCount: String) : PushUpsState()
    object SuccessfulSavingTraining: PushUpsState()
    class FinishTraining(val pushUpsLeft: String) : PushUpsState()
    object HttpError : PushUpsState()
    object NetworkError : PushUpsState()
    object UnknownError : PushUpsState()
}