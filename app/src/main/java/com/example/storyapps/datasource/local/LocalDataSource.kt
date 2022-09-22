package com.example.storyapps.datasource.local

import androidx.lifecycle.LiveData
import com.example.storyapps.datasource.local.entity.StoryEntity

class LocalDataSource private constructor(private val storyDao: StoryDao) {
    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(storyDao: StoryDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(storyDao)
    }

    fun loadStoriesBooked(): LiveData<List<StoryEntity>> =
        storyDao.loadStoriesBooked()

    fun loadStoryBookedById(id: String): LiveData<StoryEntity> =
        storyDao.loadStoryBookedById(id)

    fun insertStory(stories: StoryEntity) =
        storyDao.insertStories(stories)

    fun deleteStoryById(id: String) =
        storyDao.deleteStoryById(id)
}