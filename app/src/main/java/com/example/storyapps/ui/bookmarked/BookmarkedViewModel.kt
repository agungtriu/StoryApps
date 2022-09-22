package com.example.storyapps.ui.bookmarked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.StoryEntity

class BookmarkedViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun loadStoriesBookmarked(): LiveData<List<StoryEntity>> =
        storyRepository.loadStoriesBooked()
}