package com.nastirlex.superfit.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.DeleteUserInfoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val deleteUserInfoUsecase: DeleteUserInfoUsecase
): ViewModel() {

    private val _mainStateLiveMutable = MutableLiveData<MainState>()
    val mainStateLiveMutable: LiveData<MainState>
        get() = _mainStateLiveMutable

    fun send(event: MainEvent) {
        when (event) {
            is SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() = viewModelScope.launch(Dispatchers.IO) {
        deleteUserInfoUsecase.execute()
        _mainStateLiveMutable.postValue(MainState.SuccessfulSignOut)
    }
}