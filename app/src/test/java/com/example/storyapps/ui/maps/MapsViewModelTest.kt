package com.example.storyapps.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.LoginPreferences
import com.example.storyapps.vo.Resource
import com.example.storyapps.vo.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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
class MapsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var preferences: LoginPreferences

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStoryResponse = DataDummy.storyEntities()
    private val dummyToken = DataDummy.token

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(preferences, storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Login Status True`() = runTest {
        val expectedResult: Flow<LoginModel> = flow { LoginModel(true, DataDummy.token) }

        `when`(preferences.getLoginStatus()).thenReturn(
            expectedResult
        )

        mapsViewModel.getLoginStatus().observeForever {
            verify(preferences).getLoginStatus()
            val actualResult = it
            assertNotNull(actualResult)
            assertTrue(actualResult.isLogin)
        }
    }

    @Test
    fun `when Login Status False`() = runTest {
        val expectedResult: Flow<LoginModel> = flow { LoginModel(false, "") }

        `when`(preferences.getLoginStatus()).thenReturn(
            expectedResult
        )

        mapsViewModel.getLoginStatus().observeForever {
            verify(preferences).getLoginStatus()
            val actualResult = it
            assertNotNull(actualResult)
            assertFalse(actualResult.isLogin)
        }
    }

    @Test
    fun `when Get Stories For Maps Should Return Success`() = runTest {
        val expectedResult = MutableLiveData<Resource<List<StoryEntity>>>()
        expectedResult.value = Resource.success(dummyStoryResponse)

        `when`(storyRepository.loadMapsStories(dummyToken)).thenReturn(expectedResult)

        val actualResult = mapsViewModel.loadMapsStory(dummyToken).getOrAwaitValue()

        verify(storyRepository).loadMapsStories(dummyToken)
        assertNotNull(actualResult.data)
        assertTrue(actualResult.status == Status.SUCCESS)
    }

    @Test
    fun `when Network Error Should Return Error`() = runTest {
        val expectedResult = MutableLiveData<Resource<List<StoryEntity>>>()
        expectedResult.value = Resource.error("Error", null)

        `when`(storyRepository.loadMapsStories(dummyToken)).thenReturn(expectedResult)

        val actualResult = mapsViewModel.loadMapsStory(dummyToken).getOrAwaitValue()

        verify(storyRepository).loadMapsStories(dummyToken)
        assertNull(actualResult.data)
        assertTrue(actualResult.status == Status.ERROR)
    }
}