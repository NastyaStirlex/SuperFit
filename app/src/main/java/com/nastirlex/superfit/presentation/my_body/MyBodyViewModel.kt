package com.nastirlex.superfit.presentation.my_body

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.net.dto.BodyParametersDto
import com.nastirlex.superfit.net.dto.PhotoDto
import com.nastirlex.superfit.net.repositoryImpl.ProfileRepositoryImpl
import com.nastirlex.superfit.presentation.utils.DateMapper
import com.nastirlex.superfit.presentation.utils.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MyBodyViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val application: Application,
    private val profileRepositoryImpl: ProfileRepositoryImpl,
) : ViewModel() {
    private val _myBodyStateLiveMutable =
        MutableLiveData(MyBodyState(ParametersStatus.EmptyParameters, null, null, null))
    val myBodyStateLiveMutable: LiveData<MyBodyState>
        get() = _myBodyStateLiveMutable

    var state: MyBodyState? = _myBodyStateLiveMutable.value

    init {
        loadData()
    }

    fun send(myBodyEvent: MyBodyEvent) {
        when (myBodyEvent) {
            is UpdateParameters -> {
                updateParameters(myBodyEvent.weight, myBodyEvent.height)
            }

            is LoadPhotoOnServer -> {
                loadPhotoOnServer(myBodyEvent.bitmap)
            }

            is LoadGalleryPhotoOnServer -> {
                loadGalleryPhotoOnServer(myBodyEvent.image)
            }
        }
    }

    private fun loadData() = viewModelScope.launch {
        val history = profileRepositoryImpl.getParametersHistory()
        val lastParameters = history.maxByOrNull { item -> item.date }

        val height = lastParameters?.height
        val weight = lastParameters?.weight

        if (height != null && weight != null) {
            _myBodyStateLiveMutable.value =
                MyBodyState(
                    ParametersStatus.SuccessfulLoadingParameters(weight, height),
                    null,
                    null,
                    null
                )
        }


        val photos = profileRepositoryImpl.getProfilePhotos().sortedBy { photo -> photo.uploaded }
        val leftPhoto = photos.firstOrNull()
        val rightPhoto = if (leftPhoto != photos.lastOrNull()) photos.lastOrNull() else null

        var leftPhotoDto: PhotoDto? = null
        var rightPhotoDto: PhotoDto? = null

        lateinit var leftBitmap: Bitmap
        lateinit var rightBitmap: Bitmap

        leftPhoto?.let {
            val response = profileRepositoryImpl.downloadProfilePhoto(it.id)

            leftBitmap = BitmapFactory.decodeByteArray(
                response.bytes(), 0, response.contentLength().toInt()
            )

            leftPhotoDto = PhotoDto(
                date = DateMapper(it.uploaded),
                id = it.id,
                bitmap = leftBitmap
            )
        }

        rightPhoto?.let {
            val response = profileRepositoryImpl.downloadProfilePhoto(it.id)

            rightBitmap = BitmapFactory.decodeByteArray(
                response.bytes(), 0, response.contentLength().toInt()
            )

            rightPhotoDto = PhotoDto(
                date = DateMapper(it.uploaded),
                id = it.id,
                bitmap = rightBitmap
            )
        }

        _myBodyStateLiveMutable.value =
            _myBodyStateLiveMutable.value?.copy(
                leftPhoto = leftPhotoDto,
                rightPhoto = rightPhotoDto
            )

        state = _myBodyStateLiveMutable.value
    }

    private fun getParameters(state: MyBodyState?) = viewModelScope.launch(Dispatchers.IO) {
        val history = profileRepositoryImpl.getParametersHistory()
        val lastParameters = history.maxByOrNull { item -> item.date }

        val height = lastParameters?.height
        val weight = lastParameters?.weight

        if (height != null && weight != null) {
            _myBodyStateLiveMutable.postValue(
                state?.copy(
                    parametersStatus = ParametersStatus.SuccessfulLoadingParameters(
                        weight,
                        height
                    )
                )

            )
        }

        //state = _myBodyStateLiveMutable.value
        Log.d("state in parameters", "$state")
    }

    private fun getProfilePhotos(state: MyBodyState?) = viewModelScope.launch(Dispatchers.IO) {
        val photos = profileRepositoryImpl.getProfilePhotos().sortedBy { photo -> photo.uploaded }
        val leftPhoto = photos.firstOrNull()
        val rightPhoto = if (leftPhoto != photos.lastOrNull()) photos.lastOrNull() else null

        var leftPhotoDto: PhotoDto? = null
        var rightPhotoDto: PhotoDto? = null

        lateinit var leftBitmap: Bitmap
        lateinit var rightBitmap: Bitmap

        leftPhoto?.let {
            val response = profileRepositoryImpl.downloadProfilePhoto(it.id)

            leftBitmap = BitmapFactory.decodeByteArray(
                response.bytes(), 0, response.contentLength().toInt()
            )

            leftPhotoDto = PhotoDto(
                date = DateMapper(it.uploaded),
                id = it.id,
                bitmap = leftBitmap
            )
        }

        rightPhoto?.let {
            val response = profileRepositoryImpl.downloadProfilePhoto(it.id)

            rightBitmap = BitmapFactory.decodeByteArray(
                response.bytes(), 0, response.contentLength().toInt()
            )

            rightPhotoDto = PhotoDto(
                date = DateMapper(it.uploaded),
                id = it.id,
                bitmap = rightBitmap
            )
        }

//        _myBodyStateLiveMutable.postValue(
//            state?.copy(
//                profilePhotos = listOf(
//                    leftPhotoDto,
//                    rightPhotoDto
//                )
//            )
//        )

        //state = _myBodyStateLiveMutable.value
    }

    private fun updateParameters(weight: Int, height: Int) = viewModelScope.launch(Dispatchers.IO) {
        profileRepositoryImpl.updateParameters(
            BodyParametersDto(
                date = Clock.System.todayIn(
                    TimeZone.currentSystemDefault()
                ).toString(),
                weight = weight,
                height = height
            )
        )
    }

    private fun decodeUri(uri: Uri) {
        _myBodyStateLiveMutable.postValue(
            state?.copy(
                uri = uri
            )
        )

        state = _myBodyStateLiveMutable.value
    }

    private fun loadPhotoOnServer(bitmap: Bitmap) = viewModelScope.launch {
        try {
            val response =
                profileRepositoryImpl.uploadProfilePhoto(convertImageFileToRequestBody(bitmap))

            _myBodyStateLiveMutable.value = _myBodyStateLiveMutable.value?.copy(
                rightPhoto = PhotoDto(
                    DateMapper(response.uploaded),
                    response.id,
                    bitmap
                )
            )
        } catch (e: Exception) {
            Log.d("errorrrr", e.message.toString())
        }
    }

    private fun loadGalleryPhotoOnServer(image: Uri) = viewModelScope.launch {
        try {
            val file: File = FileUtils.getFile(application.applicationContext, image)

            val requestFile = RequestBody.create(
                application.contentResolver.getType(image)?.toMediaTypeOrNull(),
                file
            )

            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val bitmap = MediaStore.Images.Media.getBitmap(
                application.contentResolver, image
            )
            val response =
                profileRepositoryImpl.uploadProfilePhoto(body)

            _myBodyStateLiveMutable.value = _myBodyStateLiveMutable.value?.copy(
                rightPhoto = PhotoDto(
                    DateMapper(response.uploaded),
                    response.id,
                    bitmap
                )
            )
        } catch (e: Exception) {
        }
    }


    private fun convertImageFileToRequestBody(bitmap: Bitmap): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        return MultipartBody.Part.createFormData(
            "file", "photo.png",
            byteArray.toRequestBody("image/png".toMediaTypeOrNull(), 0, byteArray.size)
        )
    }
}