package com.example.storyapps.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.LoginPreferences
import com.example.storyapps.vo.Resource

class MapsViewModel(
    private val pref: LoginPreferences, private val storyRepository: StoryRepository
) : ViewModel() {
    fun getLoginStatus(): LiveData<LoginModel> {
        return pref.getLoginStatus().asLiveData()
    }

    fun loadMapsStory(token: String): LiveData<Resource<List<StoryEntity>>> =
        storyRepository.loadMapsStories(token)
}