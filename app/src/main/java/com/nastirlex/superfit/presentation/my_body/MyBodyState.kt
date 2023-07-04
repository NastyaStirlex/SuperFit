package com.nastirlex.superfit.presentation.my_body

import android.net.Uri
import com.nastirlex.superfit.net.dto.PhotoDto

data class MyBodyState(
    val parametersStatus: ParametersStatus,
    val leftPhoto: PhotoDto?,
    val rightPhoto: PhotoDto?,
    val uri: Uri?
)

sealed class ParametersStatus {
    object EmptyParameters : ParametersStatus()
    class SuccessfulLoadingParameters(
        val weight: Int,
        val height: Int
    ) : ParametersStatus()

    object HttpError : ParametersStatus()
    object NetworkError : ParametersStatus()
    object UnknownError : ParametersStatus()
}