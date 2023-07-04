package com.nastirlex.superfit.presentation.running

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.GetRunningMetersUseCase
import com.nastirlex.superfit.domain.IncreaseRunningMetersUseCase
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
class RunningViewModel @Inject constructor(
    private val trainingRepositoryImpl: TrainingRepositoryImpl,
    private val getRunningMetersUseCase: GetRunningMetersUseCase,
    private val increaseRunningMetersUseCase: IncreaseRunningMetersUseCase
): ViewModel() {

    private val _runningStateLiveMutable = MutableLiveData<RunningState>()
    val runningStateLiveMutable: LiveData<RunningState>
        get() = _runningStateLiveMutable

    private fun getRunningMeters() {
        val runningMeters = getRunningMetersUseCase.execute()

        _runningStateLiveMutable.value =
            RunningState.RunningMetersFromStorage(runningMeters.toString())
    }

    init {
        getRunningMeters()
    }

    fun send(runningEvent: RunningEvent) {
        when (runningEvent) {
            is SaveTraining -> {
                saveTraining()
            }

            is FinishTraining -> {
                finishTraining(runningEvent.runningMetersLeft)
            }
        }
    }

    private fun saveTraining() =
        viewModelScope.launch(Dispatchers.IO) {
            val pattern = "yyyy-MM-dd"
            val date = System.currentTimeMillis()
            val dtlong = Date(date)
            val sdfdate = SimpleDateFormat(pattern, Locale.getDefault()).format(dtlong)

            val runningMeters = getRunningMetersUseCase.execute()

            try {
                trainingRepositoryImpl.saveTraining(
                    TrainingDto(
                        sdfdate,
                        ExerciseType.RUNNING.toString(),
                        runningMeters
                    )
                )

                val newRunningMeters = runningMeters + Constants.RUNNING_INCREASE_VALUE

                increaseRunningMetersUseCase.execute(newRunningMeters)

                _runningStateLiveMutable.postValue(RunningState.SuccessfulSavingTraining)
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _runningStateLiveMutable.postValue(RunningState.HttpError)
                    }

                    is UnknownHostException, is SocketException -> {
                        _runningStateLiveMutable.postValue(RunningState.NetworkError)
                    }

                    else -> {
                        _runningStateLiveMutable.postValue(RunningState.UnknownError)
                    }
                }
            }
        }

    private fun finishTraining(runningMetersLeft: Int) = viewModelScope.launch(Dispatchers.IO) {
        val runningMetersTotal = getRunningMetersUseCase.execute()

        val runningMetersPassed = runningMetersTotal - runningMetersLeft

        if (runningMetersPassed > 0) {
            val pattern = "yyyy-MM-dd"
            val date = System.currentTimeMillis()
            val dtlong = Date(date)
            val sdfdate = SimpleDateFormat(pattern, Locale.getDefault()).format(dtlong)

            try {
                trainingRepositoryImpl.saveTraining(
                    TrainingDto(
                        sdfdate,
                        ExerciseType.RUNNING.toString(),
                        runningMetersPassed
                    )
                )
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _runningStateLiveMutable.postValue(RunningState.HttpError)
                    }

                    is UnknownHostException, is SocketException -> {
                        _runningStateLiveMutable.postValue(RunningState.NetworkError)
                    }

                    else -> {
                        _runningStateLiveMutable.postValue(RunningState.UnknownError)
                    }
                }
            }
        }

        _runningStateLiveMutable.postValue(RunningState.FinishTraining(runningMetersLeft.toString()))
    }
}