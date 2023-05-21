package com.nastirlex.superfit.presentation.launch

sealed class LaunchState {
    object NavigateToSignIn : LaunchState()
    object NavigateToSignUp : LaunchState()
}