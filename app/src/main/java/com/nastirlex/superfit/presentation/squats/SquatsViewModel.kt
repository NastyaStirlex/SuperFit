package com.nastirlex.superfit.presentation.squats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.GetSquatsCountUseCase
import com.nastirlex.superfit.domain.IncreaseSquatsCountUseCase
import com.nastirlex.superfit.net.dto.TrainingDto
import com.nastirlex.superfit.net.repositoryImpl.TrainingRepositoryImpl
import com.nastirlex.superfit.presentation.crunch.CrunchState
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
class SquatsViewModel @Inject constructor(
    private val trainingRepositoryImpl: TrainingRepositoryImpl,
    private val getSquatsCountUseCase: GetSquatsCountUseCase,
    private val increaseSquatsCountUseCase: IncreaseSquatsCountUseCase
) : ViewModel() {

    private val _squatsStateLiveMutable = MutableLiveData<SquatsState>()
    val squatsStateLiveMutable: LiveData<SquatsState>
        get() = _squatsStateLiveMutable

    private fun getSquatsCount() {
        val crunchCount = getSquatsCountUseCase.execute()

        _squatsStateLiveMutable.value =
            SquatsState.SquatsCountFromStorage(crunchCount.toString())
    }

    init {
        getSquatsCount()
    }

    fun send(squatsEvent: SquatsEvent) {
        when (squatsEvent) {
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
            val squatsCount = getSquatsCountUseCase.execute()

            try {
                trainingRepositoryImpl.saveTraining(
                    TrainingDto(
                        sdfdate,
                        ExerciseType.SQUATS.toString(),
                        squatsCount
                    )
                )

                val newSquatsCount = squatsCount + Constants.EXERCISES_INCREASE_VALUE

                increaseSquatsCountUseCase.execute(newSquatsCount)

                _squatsStateLiveMutable.postValue(SquatsState.SuccessfulSavingTraining)
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _squatsStateLiveMutable.postValue(SquatsState.HttpError)
                    }

                    is UnknownHostException, is SocketException -> {
                        _squatsStateLiveMutable.postValue(SquatsState.NetworkError)
                    }

                    else -> {
                        _squatsStateLiveMutable.postValue(SquatsState.UnknownError)
                    }
                }
            }
        }
}