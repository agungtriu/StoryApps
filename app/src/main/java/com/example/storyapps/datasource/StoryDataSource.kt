package com.example.storyapps.datasource

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.storyapps.datasource.local.entity.*
import com.example.storyapps.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryDataSource {
    fun registerAccount(registerBody: RegisterBody): LiveData<Resource<RegisterEntity>>

    fun loginAccount(loginBody: LoginBody): LiveData<Resource<LoginEntity>>

    fun addStory(
        imageFile: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        token: String
    ): LiveData<Resource<AddStoryEntity>>

    fun loadStoriesBooked(): LiveData<PagingData<StoryFavoriteEntity>>

    fun loadStoryBookedById(id: String): LiveData<StoryFavoriteEntity>

    fun insertStory(storyFavoriteEntity: StoryFavoriteEntity)

    fun deleteStoryById(id: String)

    fun loadStories(token: String): LiveData<PagingData<StoryEntity>>

    fun loadMapsStories(token: String): LiveData<Resource<List<StoryEntity>>>
}