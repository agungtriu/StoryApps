package com.example.storyapps.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.AddStoryEntity
import com.example.storyapps.vo.Resource
import com.example.storyapps.vo.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AddStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyAddStoryResponse = DataDummy.addStoryResult()
    private val dummyMultiPartRequest = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Description")
    private val dummyLat = DataDummy.requestBody("2")
    private val dummyLon = DataDummy.requestBody("2")
    private val dummyToken = DataDummy.token

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Upload Story Should Return Success`() = runTest {
        val expectedResult = MutableLiveData<Resource<AddStoryEntity>>()
        expectedResult.value = Resource.success(dummyAddStoryResponse)

        `when`(
            storyRepository.addStory(
                dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
            )
        ).thenReturn(expectedResult)

        val actualResult = addStoryViewModel.addStory(
            dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
        ).getOrAwaitValue()

        verify(storyRepository).addStory(
            dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
        )
        assertNotNull(actualResult.data)
        assertTrue(actualResult.status == Status.SUCCESS)
    }

    @Test
    fun `when Upload Story Network Error Should Return Error`() {
        val expectedResult = MutableLiveData<Resource<AddStoryEntity>>()
        expectedResult.value = Resource.error("Error", null)

        `when`(
            storyRepository.addStory(
                dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
            )
        ).thenReturn(expectedResult)

        val actualResult = addStoryViewModel.addStory(
            dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
        ).getOrAwaitValue()

        verify(storyRepository).addStory(
            dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
        )
        assertNull(actualResult.data)
        assertTrue(actualResult.status == Status.ERROR)
    }
}