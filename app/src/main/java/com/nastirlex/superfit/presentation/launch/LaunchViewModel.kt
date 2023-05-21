package com.nastirlex.superfit.presentation.launch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.R
import com.nastirlex.superfit.domain.GetCodeUseCase
import com.nastirlex.superfit.domain.GetTokenUseCase
import com.nastirlex.superfit.domain.GetUsernameUseCase
import com.nastirlex.superfit.domain.SaveUserInfoUseCase
import com.nastirlex.superfit.net.EncryptedSharedPref
import com.nastirlex.superfit.net.dto.AuthorizationBodyDto
import com.nastirlex.superfit.net.dto.RefreshTokenBodyDto
import com.nastirlex.superfit.net.repositoryImpl.AuthRepositoryImpl
import com.nastirlex.superfit.presentation.main.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val encryptedSharedPref: EncryptedSharedPref
) : ViewModel() {

    private val isFirstRun = encryptedSharedPref.getFirstRun()

    init {
        setupNavigation()
    }

    private val _launchStateLiveMutable = MutableLiveData<LaunchState>()
    val launchStateLiveMutable: LiveData<LaunchState>
        get() = _launchStateLiveMutable

    private fun setupNavigation() = viewModelScope.launch(Dispatchers.IO) {
        delay(3000)
        if (isFirstRun) {
            encryptedSharedPref.saveFirstRun(false)
            _launchStateLiveMutable.postValue(LaunchState.NavigateToSignUp)
        } else {
            _launchStateLiveMutable.postValue(LaunchState.NavigateToSignIn)
        }
    }
}