package com.example.storyapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.LoginBody
import com.example.storyapps.datasource.local.entity.LoginEntity
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.ui.LoginPreferences
import com.example.storyapps.vo.Resource
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: LoginPreferences, private val storyRepository: StoryRepository
) : ViewModel() {

    fun saveLoginStatus(loginModel: LoginModel) {
        viewModelScope.launch {
            pref.saveLoginStatus(loginModel)
        }
    }

    fun loginAccount(loginBody: LoginBody): LiveData<Resource<LoginEntity>> =
        storyRepository.loginAccount(loginBody)
}