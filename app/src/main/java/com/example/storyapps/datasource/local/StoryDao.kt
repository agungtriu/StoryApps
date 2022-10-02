package com.example.storyapps.datasource.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM story_favorite_entities")
    fun loadStoriesBooked(): PagingSource<Int, StoryFavoriteEntity>

    @Query("SELECT * FROM story_favorite_entities")
    fun loadStoriesWidget(): List<StoryFavoriteEntity>

    @Query("SELECT * FROM story_favorite_entities WHERE id = :id")
    fun loadStoryBookedById(id: String): LiveData<StoryFavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStoryBooked(stories: StoryFavoriteEntity)

    @Query("DELETE FROM story_favorite_entities WHERE id = :id")
    fun deleteStoryById(id: String)

    @Query("SELECT * FROM story_entities")
    fun loadStories(): PagingSource<Int, StoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Query("DELETE FROM story_entities")
    suspend fun deleteAll()
}