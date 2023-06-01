package com.nastirlex.superfit.presentation.crunch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.domain.GetCrunchUseCase
import com.nastirlex.superfit.domain.IncreaseCrunchCountUseCase
import com.nastirlex.superfit.net.dto.TrainingDto
import com.nastirlex.superfit.net.repositoryImpl.TrainingRepositoryImpl
import com.nastirlex.superfit.presentation.signin.SignInState
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
class CrunchViewModel @Inject constructor(
    private val trainingRepositoryImpl: TrainingRepositoryImpl,
    private val getCrunchUseCase: GetCrunchUseCase,
     private val increaseCrunchCountUseCase: IncreaseCrunchCountUseCase
) : ViewModel() {

    private val _crunchStateLiveMutable = MutableLiveData<CrunchState>()
    val crunchStateLiveMutable: LiveData<CrunchState>
        get() = _crunchStateLiveMutable

    private fun getCrunchCount() {
        val crunchCount = getCrunchUseCase.execute()

        _crunchStateLiveMutable.value =
            CrunchState.CrunchCountFromStorage(crunchCount.toString())
    }

    init {
        getCrunchCount()
    }

    fun send(crunchEvent: CrunchEvent) {
        when (crunchEvent) {
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
            val crunchCount = getCrunchUseCase.execute()

            try {
                trainingRepositoryImpl.saveTraining(
                    TrainingDto(
                        sdfdate,
                        ExerciseType.CRUNCH.toString(),
                        crunchCount
                    )
                )

                val newCrunchCount = crunchCount + Constants.EXERCISES_INCREASE_VALUE

                increaseCrunchCountUseCase.execute(newCrunchCount)

                _crunchStateLiveMutable.postValue(CrunchState.SuccessfulSavingTraining)
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        _crunchStateLiveMutable.postValue(CrunchState.HttpError)
                    }

                    is UnknownHostException, is SocketException -> {
                        _crunchStateLiveMutable.postValue(CrunchState.NetworkError)
                    }

                    else -> {
                        _crunchStateLiveMutable.postValue(CrunchState.UnknownError)
                    }
                }
            }
        }
}