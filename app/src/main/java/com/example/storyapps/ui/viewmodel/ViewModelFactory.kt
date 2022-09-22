package com.example.storyapps.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.ui.LoginPreferences
import com.example.storyapps.ui.add.AddStoryViewModel
import com.example.storyapps.ui.bookmarked.BookmarkedViewModel
import com.example.storyapps.ui.detail.DetailViewModel
import com.example.storyapps.ui.di.Injection
import com.example.storyapps.ui.home.HomeViewModel
import com.example.storyapps.ui.login.LoginViewModel
import com.example.storyapps.ui.register.RegisterViewModel
import com.example.storyapps.ui.setting.SettingViewModel
import com.example.storyapps.ui.splashscreen.SplashScreenViewModel

class ViewModelFactory private constructor(
    private val preferences: LoginPreferences, private val repository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideLoginPreference(context), Injection.provideRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(preferences) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(preferences, repository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(preferences, repository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(BookmarkedViewModel::class.java)) {
            return BookmarkedViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}