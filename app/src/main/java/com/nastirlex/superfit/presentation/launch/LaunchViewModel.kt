package com.nastirlex.superfit.presentation.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.net.EncryptedSharedPref
import com.nastirlex.superfit.presentation.utils.Constants
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
    private val isThereAccessToken =
        encryptedSharedPref.getAccessToken() != Constants.CREDENTIALS_DEFAULT_VALUE

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
        } else if (isThereAccessToken) {
            _launchStateLiveMutable.postValue(LaunchState.NavigateToMain)
        } else {
            _launchStateLiveMutable.postValue(LaunchState.NavigateToSignIn)
        }
    }
}