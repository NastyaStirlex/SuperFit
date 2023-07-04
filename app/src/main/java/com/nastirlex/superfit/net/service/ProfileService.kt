package com.nastirlex.superfit.net.service

import com.nastirlex.superfit.net.dto.BodyParametersDto
import com.nastirlex.superfit.net.dto.ProfilePhotoDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import okhttp3.ResponseBody

interface ProfileService {
    @POST("api/profile/params")
    suspend fun updateParameters(@Body bodyParameters: BodyParametersDto)

    @GET("api/profile/params/history")
    suspend fun getParametersHistory(): List<BodyParametersDto>

    @GET("api/profile/photos")
    suspend fun getProfilePhotos(): List<ProfilePhotoDto>

    @Multipart
    @POST("api/profile/photos")
    suspend fun uploadProfilePhoto(@Part file: MultipartBody.Part): ProfilePhotoDto

    @GET("api/profile/photos/{id}")
    suspend fun downloadProfilePhotos(@Path("id") id: String): ResponseBody

    @DELETE("api/profile/photos/{id}")
    suspend fun deleteProfilePhoto(@Path("id") id : String)
}