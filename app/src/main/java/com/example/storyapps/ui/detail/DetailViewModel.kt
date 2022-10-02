package com.example.storyapps.ui.detail

import androidx.lifecycle.ViewModel
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity

class DetailViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun loadStoryBookedById(id: String) = storyRepository.loadStoryBookedById(id)

    fun insertStory(storyFavoriteEntity: StoryFavoriteEntity) =
        storyRepository.insertStory(storyFavoriteEntity)

    fun deleteStory(id: String) = storyRepository.deleteStoryById(id)
}