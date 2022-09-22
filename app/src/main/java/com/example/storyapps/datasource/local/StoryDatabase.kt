package com.example.storyapps.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.utils.Config.Companion.DATABASE_NAME

@Database(
    entities = [StoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase(){
    abstract fun storyDao(): StoryDao

    companion object {

        @Volatile
        private var INSTANCE: StoryDatabase? = null

        fun getInstance(context: Context): StoryDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    DATABASE_NAME
                ).build().apply {
                    INSTANCE = this
                }
            }
    }
}