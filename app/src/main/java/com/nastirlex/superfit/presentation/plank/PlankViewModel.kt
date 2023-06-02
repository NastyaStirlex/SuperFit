package com.nastirlex.superfit.presentation.plank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.GetPlankTimeUseCase
import com.nastirlex.superfit.domain.IncreasePlankTimeUseCase
import com.nastirlex.superfit.net.dto.TrainingDto
import com.nastirlex.superfit.net.repositoryImpl.TrainingRepositoryImpl
import com.nastirlex.superfit.presentation.squats.SquatsState
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
class PlankViewModel @Inject constructor(
    private val trainingRepositoryImpl: TrainingRepositoryImpl,
    private val getPlankTimeUseCase: GetPlankTimeUseCase,
    private val increasePlankTimeUseCase: IncreasePlankTimeUseCase
): ViewModel() {

    private val _plankStateLiveMutable = MutableLiveData<PlankState>()
    val plankStateLiveMutable: LiveData<PlankState>
        get() = _plankStateLiveMutable

    private fun getPlankTime() {
        val plankTime = getPlankTimeUseCase.execute()

        _plankStateLiveMutable.value =
            PlankState.PlankTimeFromStorage(plankTime.toString())
    }

    init {
        getPlankTime()
    }

    fun send(plankEvent: PlankEvent) {
        when (plankEvent) {
            is SaveTraining -> {
                saveTraining()
            }
        }
    }

    private fun saveTraining() =
        viewModelScope.launch(Dispatchers.IO) {
            val pattern = "yyyy-MM-dd"
            val date = System.currentTimeMillis()
            val dtlong = Date(date)
            val sdfdate = SimpleDateFormat(pattern, Locale.getDefault()).format(dtlong)
            val plankTime = getPlankTimeUseCase.execute()

            try {
                trainingRepositoryImpl.saveTraining(
                    TrainingDto(
                        sdfdate,
                        ExerciseType.PLANK.toString(),
                        plankTime
                    )
                )

                val newPlankTime = plankTime + Constants.EXERCISES_INCREASE_VALUE

                increasePlankTimeUseCase.execute(newPlankTime)

                _plankStateLiveMutable.postValue(PlankState.SuccessfulSavingTraining)
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _plankStateLiveMutable.postValue(PlankState.HttpError)
                    }

                    is UnknownHostException, is SocketException -> {
                        _plankStateLiveMutable.postValue(PlankState.NetworkError)
                    }

                    else -> {
                        _plankStateLiveMutable.postValue(PlankState.UnknownError)
                    }
                }
            }
        }
}