package com.example.storyapps.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.AddStoryEntity
import com.example.storyapps.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun addStory(
        imageFile: MultipartBody.Part, description: RequestBody, token: String
    ): LiveData<Resource<AddStoryEntity>> = storyRepository.addStory(imageFile, description, token)
}