package com.example.storyapps.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapps.ui.LoginPreferences
import kotlinx.coroutines.launch

class SettingViewModel(
    private val pref: LoginPreferences
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            pref.deleteLoginStatus()
        }
    }
}