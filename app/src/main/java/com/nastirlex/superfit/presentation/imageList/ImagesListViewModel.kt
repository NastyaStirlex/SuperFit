package com.nastirlex.superfit.presentation.imageList

import android.graphics.BitmapFactory
import android.provider.Contacts.Photos
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastirlex.superfit.net.dto.PhotoDto
import com.nastirlex.superfit.net.repositoryImpl.ProfileRepositoryImpl
import com.nastirlex.superfit.presentation.main.MainState
import com.nastirlex.superfit.presentation.utils.DateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagesListViewModel @Inject constructor(
    private val profileRepositoryImpl: ProfileRepositoryImpl
) : ViewModel() {
    private val _imagesListStateLiveMutable = MutableLiveData<ImagesListState>()
    val imagesListStateLiveMutable: LiveData<ImagesListState>
        get() = _imagesListStateLiveMutable

    init {
        getProfile()
    }

    private fun getProfile() = viewModelScope.launch {
        val photos = profileRepositoryImpl.getProfilePhotos()

        if (photos.isNotEmpty()) {
            val photoList = mutableListOf<PhotoDto>()

            photos.forEach { photoId ->
                val photo = profileRepositoryImpl.downloadProfilePhoto(photoId.id)
                val bitmap = BitmapFactory.decodeByteArray(
                    photo.bytes(),
                    0,
                    photo.contentLength().toInt()
                )

                photoList.add(
                    PhotoDto(
                        DateMapper(photoId.uploaded),
                        photoId.id,
                        bitmap
                    )
                )
            }

            val data = mutableMapOf<String, MutableList<PhotoDto>>()

            var lastYear = photoList.firstOrNull()?.date?.substring(0, 4)
            var lastMonth = photoList.firstOrNull()?.date?.substring(5, 7)


            photoList.sortedBy { item -> item.date }.forEach { photoData ->
                val photoYear = photoData.date.substring(0, 4)
                val photoMonth = photoData.date.substring(5, 7)
                if (photoYear == lastYear){
                    if (photoMonth == lastMonth){
                        if (data["$lastYear $lastMonth"] == null){
                            data["$lastYear $lastMonth"] = mutableListOf(photoData)
                        } else{
                            data["$lastYear $lastMonth"]?.add(photoData)
                        }
                        lastYear = photoYear
                        lastMonth = photoMonth
                    } else{
                        data["$photoYear $photoMonth"] = mutableListOf(photoData)
                        lastYear = photoYear
                        lastMonth = photoMonth
                    }
                } else {
                    data["$photoYear $photoMonth"] = mutableListOf(photoData)
                    lastYear = photoYear
                    lastMonth = photoMonth
                }
            }

            _imagesListStateLiveMutable.value = ImagesListState.FullImagesList(data)

        }


    }
}