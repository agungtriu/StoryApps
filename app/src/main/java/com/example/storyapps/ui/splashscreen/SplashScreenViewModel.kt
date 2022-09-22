package com.example.storyapps.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.ui.LoginPreferences

class SplashScreenViewModel(private val pref: LoginPreferences) : ViewModel() {
    fun getLoginStatus(): LiveData<LoginModel> {
        return pref.getLoginStatus().asLiveData()
    }
}