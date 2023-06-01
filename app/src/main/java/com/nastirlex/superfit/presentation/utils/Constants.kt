package com.nastirlex.superfit.presentation.utils

object Constants {
    // Encrypted Shared Preferences
    const val ACCESS_TOKEN_KEY = "accessToken"
    const val USERNAME_KEY = "username"
    const val PASSWORD_KEY = "password"
    const val FIRST_RUN_KEY = "firstRun"
    const val CREDENTIALS_DEFAULT_VALUE = "empty"
    const val FIRST_RUN_DEFAULT_VALUE = true

    const val CRUNCH_KEY = "crunch"
    const val SQUATS_KEY = "squats"
    const val PLANK_KEY = "plank"
    const val PUSH_UPS_KEY = "pushUps"
    const val RUNNING_KEY = "running"
    const val EXERCISES_DEFAULT_VALUE = 10
    const val RUNNING_DEFAULT_VALUE = 1000
    const val PLANK_DEFAULT_VALUE = 20
    const val EXERCISES_INCREASE_VALUE = 5
    const val RUNNING_INCREASE_VALUE = 100

    val EMAIL_REGEXP = Regex("^(([^-A-Z_<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-z0-9]+\\.)+[a-z]{2,}))\$")
}