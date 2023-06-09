package com.nastirlex.superfit.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.DeleteExercisesProgressUseCase
import com.nastirlex.superfit.domain.DeleteUserInfoUseCase
import com.nastirlex.superfit.net.repositoryImpl.TrainingRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val trainingRepositoryImpl: TrainingRepositoryImpl,
    private val deleteUserInfoUseCase: DeleteUserInfoUseCase,
    private val deleteExercisesProgressUseCase: DeleteExercisesProgressUseCase
): ViewModel() {

    private val _mainStateLiveMutable = MutableLiveData<MainState>()
    val mainStateLiveMutable: LiveData<MainState>
        get() = _mainStateLiveMutable

    init {
        getLastExercises()
    }

    fun send(event: MainEvent) {
        when (event) {
            is SignOut -> {
                signOut()
            }

        }
    }

    private fun signOut() = viewModelScope.launch(Dispatchers.IO) {
        deleteUserInfoUseCase.execute()
        deleteExercisesProgressUseCase.execute()
        _mainStateLiveMutable.postValue(MainState.SuccessfulSignOut)
    }

    private fun getLastExercises() = viewModelScope.launch(Dispatchers.IO) {
        val lastExercises = trainingRepositoryImpl.getTrainings()
        if (lastExercises.isEmpty() || lastExercises.size < 2) {
            _mainStateLiveMutable.postValue(MainState.LastExercisesEmpty)
        } else {
            _mainStateLiveMutable.postValue(MainState.LastExercisesLoaded(lastExercises))
        }
    }
}