package com.example.storyapps.ui.di

import android.content.Context
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.LocalDataSource
import com.example.storyapps.datasource.local.StoryDatabase
import com.example.storyapps.datasource.remote.RemoteDataSource
import com.example.storyapps.ui.LoginPreferences
import com.example.storyapps.ui.login.dataStore
import com.example.storyapps.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.storyDao())
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideLoginPreference(context: Context): LoginPreferences {
        return LoginPreferences.getInstance(context.dataStore)
    }
}