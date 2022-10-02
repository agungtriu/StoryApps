package com.example.storyapps.ui.bookmarked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity

class BookmarkedViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun loadStoriesBookmarked(): LiveData<PagingData<StoryFavoriteEntity>> =
        storyRepository.loadStoriesBooked().cachedIn(viewModelScope)
}