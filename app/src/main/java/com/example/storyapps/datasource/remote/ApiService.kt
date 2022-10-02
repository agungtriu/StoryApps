package com.example.storyapps.datasource.remote

import com.example.storyapps.datasource.local.entity.LoginBody
import com.example.storyapps.datasource.local.entity.RegisterBody
import com.example.storyapps.datasource.remote.response.AddStoryResponse
import com.example.storyapps.datasource.remote.response.LoginResponse
import com.example.storyapps.datasource.remote.response.RegisterResponse
import com.example.storyapps.datasource.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun registerAccount(
        @Body registerBody: RegisterBody
    ): Call<RegisterResponse>

    @POST("login")
    fun loginAccount(
        @Body loginBody: LoginBody
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Header("Authorization") token: String
    ): Call<AddStoryResponse>

    @GET("stories")
    suspend fun loadStory(
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Header("Authorization") token: String
    ): StoryResponse

    @GET("stories")
    fun loadMapsStory(
        @Query("location") location: Int = 1,
        @Header("Authorization") token: String
    ): Call<StoryResponse>
}