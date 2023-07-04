package com.nastirlex.superfit.presentation.utils

import kotlinx.datetime.Instant

fun DateMapper(time: Int): String =
    Instant.fromEpochMilliseconds(time * 1000L)
        .toString().subSequence(0, 10) as String