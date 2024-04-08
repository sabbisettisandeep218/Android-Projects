package com.example.demo_app

import com.google.firebase.appdistribution.gradle.models.UploadResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.File

interface ApiService {

    @Multipart
    @POST("files/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ):Call<Demo>

    @GET
    @Streaming
    fun downloadFile(@Url fileUrl: String): ResponseBody

}