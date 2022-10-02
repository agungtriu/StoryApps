package com.example.storyapps.ui.splashscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapps.DataDummy
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.ui.LoginPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashScreenViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var preferences: LoginPreferences

    private lateinit var splashScreenViewModel: SplashScreenViewModel

    @Before
    fun setUp() {
        splashScreenViewModel = SplashScreenViewModel(preferences)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Login Status True`() = runTest {
        val expectedResult: Flow<LoginModel> = flow { LoginModel(true, DataDummy.token) }

        `when`(preferences.getLoginStatus()).thenReturn(
            expectedResult
        )

        splashScreenViewModel.getLoginStatus().observeForever {
            verify(preferences).getLoginStatus()
            val actualResult = it
            Assert.assertNotNull(actualResult)
            Assert.assertTrue(actualResult.isLogin)
        }
    }

    @Test
    fun `when Login Status False`() = runTest {
        val expectedResult: Flow<LoginModel> = flow { LoginModel(false, "") }

        `when`(preferences.getLoginStatus()).thenReturn(
            expectedResult
        )

        splashScreenViewModel.getLoginStatus().observeForever {
            verify(preferences).getLoginStatus()
            val actualResult = it
            Assert.assertNotNull(actualResult)
            Assert.assertFalse(actualResult.isLogin)
        }
    }
}