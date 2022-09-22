package com.example.storyapps.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.RegisterBody
import com.example.storyapps.datasource.local.entity.RegisterEntity
import com.example.storyapps.vo.Resource

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun registerAccount(registerBody: RegisterBody): LiveData<Resource<RegisterEntity>> =
        storyRepository.registerAccount(registerBody)
}