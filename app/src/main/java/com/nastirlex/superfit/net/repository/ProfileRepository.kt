package com.nastirlex.superfit.net.repository

import com.nastirlex.superfit.net.dto.BodyParametersDto
import com.nastirlex.superfit.net.dto.ProfilePhotoDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface ProfileRepository {
    suspend fun updateParameters(bodyParameters: BodyParametersDto)

    suspend fun getParametersHistory(): List<BodyParametersDto>

    suspend fun getProfilePhotos(): List<ProfilePhotoDto>

    suspend fun uploadProfilePhoto(file: MultipartBody.Part): ProfilePhotoDto

    suspend fun downloadProfilePhoto(id: String): ResponseBody

    suspend fun deleteProfilePhoto(id : String)
}