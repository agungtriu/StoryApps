package com.example.storyapps.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapps.datasource.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM storyentities")
    fun loadStoriesBooked(): LiveData<List<StoryEntity>>

    @Query("SELECT * FROM storyentities")
    fun loadStoriesWidget(): List<StoryEntity>

    @Query("SELECT * FROM storyentities WHERE id = :id")
    fun loadStoryBookedById(id: String): LiveData<StoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStories(stories: StoryEntity)

    @Query("DELETE FROM storyentities WHERE id = :id")
    fun deleteStoryById(id: String)
}