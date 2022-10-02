package com.example.storyapps.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.LoginPreferences

class HomeViewModel(
    private val pref: LoginPreferences, private val storyRepository: StoryRepository
) : ViewModel() {

    fun getLoginStatus(): LiveData<LoginModel> {
        return pref.getLoginStatus().asLiveData()
    }

    fun loadStory(token: String): LiveData<PagingData<StoryEntity>> =
        storyRepository.loadStories(token).cachedIn(viewModelScope)
}