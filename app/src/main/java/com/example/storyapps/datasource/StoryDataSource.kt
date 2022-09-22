package com.example.storyapps.datasource

import androidx.lifecycle.LiveData
import com.example.storyapps.datasource.local.entity.*
import com.example.storyapps.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryDataSource {
    fun registerAccount(registerBody: RegisterBody): LiveData<Resource<RegisterEntity>>

    fun loginAccount(loginBody: LoginBody): LiveData<Resource<LoginEntity>>

    fun addStory(
        imageFile: MultipartBody.Part, description: RequestBody, token: String
    ): LiveData<Resource<AddStoryEntity>>

    fun loadStories(page: Int, token: String): LiveData<Resource<List<StoryEntity>>>

    fun loadStoriesBooked(): LiveData<List<StoryEntity>>

    fun loadStoryBookedById(id: String): LiveData<StoryEntity>

    fun insertStory(storyEntity: StoryEntity)

    fun deleteStoryById(id: String)
}