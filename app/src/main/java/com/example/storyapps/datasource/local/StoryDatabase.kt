package com.example.storyapps.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapps.datasource.local.entity.RemoteKeys
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity
import com.example.storyapps.utils.Config.Companion.DATABASE_NAME

@Database(
    entities = [StoryEntity::class, StoryFavoriteEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext, StoryDatabase::class.java, DATABASE_NAME
            ).fallbackToDestructiveMigration().build().apply {
                INSTANCE = this
            }
        }
    }
}