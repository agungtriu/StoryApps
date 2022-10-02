package com.example.storyapps.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.StoryPagingSource
import com.example.storyapps.Utils.Companion.noopListUpdateCallback
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.LoginPreferences
import kotlinx.coroutines.Dispatchers
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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var preferences: LoginPreferences

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var homeViewModel: HomeViewModel
    private val dummyToken = DataDummy.token
    private val dummyStory = DataDummy.storyEntities()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(preferences, storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Login Status True`() = runTest {
        val expectedResult: Flow<LoginModel> = flow { LoginModel(true, DataDummy.token) }

        Mockito.`when`(preferences.getLoginStatus()).thenReturn(
            expectedResult
        )

        homeViewModel.getLoginStatus().observeForever {
            Mockito.verify(preferences).getLoginStatus()
            val actualResult = it
            assertNotNull(actualResult)
            assertTrue(actualResult.isLogin)
        }
    }

    @Test
    fun `when Login Status False`() = runTest {
        val expectedResult: Flow<LoginModel> = flow { LoginModel(false, "") }

        Mockito.`when`(preferences.getLoginStatus()).thenReturn(
            expectedResult
        )

        homeViewModel.getLoginStatus().observeForever {
            Mockito.verify(preferences).getLoginStatus()
            val actualResult = it
            assertNotNull(actualResult)
            assertFalse(actualResult.isLogin)
        }
    }

    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expected = MutableLiveData<PagingData<StoryEntity>>()
        expected.value = data

        Mockito.`when`(storyRepository.loadStories(dummyToken)).thenReturn(expected)

        val actualStory: PagingData<StoryEntity> =
            homeViewModel.loadStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
    }
}