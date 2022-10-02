package com.example.storyapps.ui.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.ui.LoginPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var preferences: LoginPreferences

    private lateinit var settingViewModel: SettingViewModel

    @Before
    fun setUp() {
        settingViewModel = SettingViewModel(preferences)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Logout`() = runTest {
        settingViewModel.logout()
        Mockito.verify(preferences).deleteLoginStatus()
    }
}