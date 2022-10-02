package com.example.storyapps.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.RegisterEntity
import com.example.storyapps.vo.Resource
import com.example.storyapps.vo.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val dummyResult = DataDummy.registerResult()
    private val dummyRegister = DataDummy.registerRequest

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Register Success`() = runTest {
        val expectedResult = MutableLiveData<Resource<RegisterEntity>>()
        expectedResult.value = Resource.success(dummyResult)

        Mockito.`when`(storyRepository.registerAccount(dummyRegister)).thenReturn(
            expectedResult
        )

        val actualResult = registerViewModel.registerAccount(dummyRegister).getOrAwaitValue()

        Mockito.verify(storyRepository).registerAccount(dummyRegister)
        Assert.assertNotNull(actualResult.data)
        Assert.assertTrue(actualResult.status == Status.SUCCESS)
    }

    @Test
    fun `when Register Failed`() = runTest {
        val expectedResult = MutableLiveData<Resource<RegisterEntity>>()
        expectedResult.value = Resource.error("", null)

        Mockito.`when`(storyRepository.registerAccount(dummyRegister)).thenReturn(
            expectedResult
        )

        val actualResult = registerViewModel.registerAccount(dummyRegister).getOrAwaitValue()

        Mockito.verify(storyRepository).registerAccount(dummyRegister)
        Assert.assertNull(actualResult.data)
        Assert.assertTrue(actualResult.status == Status.ERROR)
    }
}