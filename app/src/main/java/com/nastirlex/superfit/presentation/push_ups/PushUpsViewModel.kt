package com.nastirlex.superfit.presentation.push_ups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.GetPushUpsCountUseCase
import com.nastirlex.superfit.domain.IncreasePushUpsCountUseCase
import com.nastirlex.superfit.net.dto.TrainingDto
import com.nastirlex.superfit.net.repositoryImpl.TrainingRepositoryImpl
import com.nastirlex.superfit.presentation.utils.Constants
import com.nastirlex.superfit.presentation.utils.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PushUpsViewModel @Inject constructor(
    private val trainingRepositoryImpl: TrainingRepositoryImpl,
    private val getPushUpsCountUseCase: GetPushUpsCountUseCase,
    private val increasePushUpsCountUseCase: IncreasePushUpsCountUseCase
) : ViewModel() {

    private val _pushUpsStateLiveMutable = MutableLiveData<PushUpsState>()
    val pushUpsStateLiveMutable: LiveData<PushUpsState>
        get() = _pushUpsStateLiveMutable

    private fun getPushUpsCount() {
        val pushUpsCount = getPushUpsCountUseCase.execute()

        _pushUpsStateLiveMutable.value =
            PushUpsState.PushUpsCountFromStorage(pushUpsCount.toString())
    }

    init {
        getPushUpsCount()
    }

    fun send(pushUpsEvent: PushUpsEvent) {
        when (pushUpsEvent) {
            is SaveTraining -> {
                saveTraining()
            }

            is FinishTraining -> {
                finishTraining(pushUpsEvent.pushUpsLeft)
            }
        }
    }

    private fun saveTraining() =
        viewModelScope.launch(Dispatchers.IO) {
            val pattern = "yyyy-MM-dd"
            val date = System.currentTimeMillis()
            val dtlong = Date(date)
            val sdfdate = SimpleDateFormat(pattern, Locale.getDefault()).format(dtlong)
            val pushUpsCount = getPushUpsCountUseCase.execute()

            try {
                trainingRepositoryImpl.saveTraining(
                    TrainingDto(
                        sdfdate,
                        ExerciseType.PUSH_UP.toString(),
                        pushUpsCount
                    )
                )

                val newPushUpsCount = pushUpsCount + Constants.EXERCISES_INCREASE_VALUE

                increasePushUpsCountUseCase.execute(newPushUpsCount)

                _pushUpsStateLiveMutable.postValue(PushUpsState.SuccessfulSavingTraining)
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _pushUpsStateLiveMutable.postValue(PushUpsState.HttpError)
                    }

                    is UnknownHostException, is SocketException -> {
                        _pushUpsStateLiveMutable.postValue(PushUpsState.NetworkError)
                    }

                    else -> {
                        _pushUpsStateLiveMutable.postValue(PushUpsState.UnknownError)
                    }
                }
            }
        }

    private fun finishTraining(pushUpsLeft: Int) = viewModelScope.launch(Dispatchers.IO) {
        val pushUpsTotal = getPushUpsCountUseCase.execute()

        val pushUpsMade = pushUpsTotal - pushUpsLeft

        if (pushUpsMade > 0) {
            val pattern = "yyyy-MM-dd"
            val date = System.currentTimeMillis()
            val dtlong = Date(date)
            val sdfdate = SimpleDateFormat(pattern, Locale.getDefault()).format(dtlong)

            try {
                trainingRepositoryImpl.saveTraining(
                    TrainingDto(
                        sdfdate,
                        ExerciseType.PUSH_UP.toString(),
                        pushUpsMade
                    )
                )
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _pushUpsStateLiveMutable.postValue(PushUpsState.HttpError)
                    }

                    is UnknownHostException, is SocketException -> {
                        _pushUpsStateLiveMutable.postValue(PushUpsState.NetworkError)
                    }

                    else -> {
                        _pushUpsStateLiveMutable.postValue(PushUpsState.UnknownError)
                    }
                }
            }
        }

        _pushUpsStateLiveMutable.postValue(PushUpsState.FinishTraining(pushUpsLeft.toString()))
    }

}