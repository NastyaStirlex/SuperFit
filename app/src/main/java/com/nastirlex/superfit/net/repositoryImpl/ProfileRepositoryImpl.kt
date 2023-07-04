package com.nastirlex.superfit.net.repositoryImpl

import com.nastirlex.superfit.net.dto.BodyParametersDto
import com.nastirlex.superfit.net.dto.ProfilePhotoDto
import com.nastirlex.superfit.net.repository.ProfileRepository
import com.nastirlex.superfit.net.service.ProfileService
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService
) : ProfileRepository {
    override suspend fun updateParameters(bodyParameters: BodyParametersDto) =
        profileService.updateParameters(bodyParameters)


    override suspend fun getParametersHistory(): List<BodyParametersDto> {
        return profileService.getParametersHistory()
    }

    override suspend fun getProfilePhotos(): List<ProfilePhotoDto> {
        return profileService.getProfilePhotos()
    }

    override suspend fun uploadProfilePhoto(file: MultipartBody.Part): ProfilePhotoDto {
        return profileService.uploadProfilePhoto(file)
    }

    override suspend fun downloadProfilePhoto(id: String): ResponseBody {
        return profileService.downloadProfilePhotos(id)
    }

    override suspend fun deleteProfilePhoto(id: String) =
        profileService.deleteProfilePhoto(id)

}