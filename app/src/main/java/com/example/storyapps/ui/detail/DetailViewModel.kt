package com.example.storyapps.ui.detail

import androidx.lifecycle.ViewModel
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.StoryEntity

class DetailViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun loadStoryBookedById(id: String) = storyRepository.loadStoryBookedById(id)

    fun insertStory(storyEntity: StoryEntity) = storyRepository.insertStory(storyEntity)

    fun deleteStory(id: String) = storyRepository.deleteStoryById(id)
}