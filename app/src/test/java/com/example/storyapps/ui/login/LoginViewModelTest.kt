package com.example.storyapps.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.LoginEntity
import com.example.storyapps.ui.LoginPreferences
import com.example.storyapps.vo.Resource
import com.example.storyapps.vo.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
class LoginViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var preferences: LoginPreferences

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyResult = DataDummy.loginResult()
    private val dummyLogin = DataDummy.loginRequest

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(preferences, storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Login Success`() = runTest {
        val expectedResult = MutableLiveData<Resource<LoginEntity>>()
        expectedResult.value = Resource.success(dummyResult)

        `when`(storyRepository.loginAccount(dummyLogin)).thenReturn(
            expectedResult
        )

        val actualResult = loginViewModel.loginAccount(dummyLogin).getOrAwaitValue()

        verify(storyRepository).loginAccount(dummyLogin)
        assertNotNull(actualResult)
        assertTrue(actualResult.status == Status.SUCCESS)
    }

    @Test
    fun `when Login Failed`() = runTest {
        val expectedResult = MutableLiveData<Resource<LoginEntity>>()
        expectedResult.value = Resource.error("", null)

        `when`(storyRepository.loginAccount(dummyLogin)).thenReturn(
            expectedResult
        )

        val actualResult = loginViewModel.loginAccount(dummyLogin).getOrAwaitValue()

        verify(storyRepository).loginAccount(dummyLogin)
        assertNotNull(actualResult)
        assertTrue(actualResult.status == Status.ERROR)
    }
}