package com.example.storyapps.datasource.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.example.storyapps.datasource.local.entity.RemoteKeys
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity

class LocalDataSource private constructor(
    private val storyDao: StoryDao, private val remoteKeysDao: RemoteKeysDao
) {
    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(storyDao: StoryDao, remoteKeysDao: RemoteKeysDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(storyDao, remoteKeysDao)
    }

    fun loadStoriesBooked(): PagingSource<Int, StoryFavoriteEntity> = storyDao.loadStoriesBooked()

    fun loadStoryBookedById(id: String): LiveData<StoryFavoriteEntity> =
        storyDao.loadStoryBookedById(id)

    fun insertStory(story: StoryFavoriteEntity) = storyDao.insertStoryBooked(story)

    fun deleteStoryById(id: String) = storyDao.deleteStoryById(id)

    fun loadStories(): PagingSource<Int, StoryEntity> = storyDao.loadStories()

    suspend fun insertStories(stories: List<StoryEntity>) = storyDao.insertStories(stories)

    suspend fun deleteAll() = storyDao.deleteAll()

    fun insertRemoteAll(remoteKeys: List<RemoteKeys>) = remoteKeysDao.insertAll(remoteKeys)

    suspend fun getRemoteKeysId(id: String) = remoteKeysDao.getRemoteKeysId(id)

    fun deleteRemoteKeys() = remoteKeysDao.deleteRemoteKeys()
}