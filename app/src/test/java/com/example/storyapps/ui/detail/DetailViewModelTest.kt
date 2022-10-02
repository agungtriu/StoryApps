package com.example.storyapps.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class DetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var detailViewModel: DetailViewModel
    private val dummyStory = DataDummy.storyFavoriteEntities()[0]
    private val dummyId = DataDummy.storyFavoriteEntities()[0].id

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Get Story from Bookmarked Should Not Null`() = runTest {
        val expected = MutableLiveData<StoryFavoriteEntity>()
        expected.value = dummyStory

        `when`(storyRepository.loadStoryBookedById(dummyId)).thenReturn(expected)

        val actualStory: StoryFavoriteEntity =
            detailViewModel.loadStoryBookedById(dummyId).getOrAwaitValue()

        verify(storyRepository).loadStoryBookedById(dummyId)

        Assert.assertNotNull(actualStory)
        Assert.assertEquals(dummyStory.id, actualStory.id)
        Assert.assertEquals(dummyStory.name, actualStory.name)
        Assert.assertEquals(dummyStory.photoUrl, actualStory.photoUrl)
        Assert.assertEquals(dummyStory.createdAt, actualStory.createdAt)
        Assert.assertEquals(dummyStory.description, actualStory.description)
        Assert.assertEquals(dummyStory.lat, actualStory.lat)
        Assert.assertEquals(dummyStory.lon, actualStory.lon)
    }

    @Test
    fun `when Get Story from Bookmarked Null`() = runTest {
        val expected = MutableLiveData<StoryFavoriteEntity>()
        expected.value = null

        `when`(storyRepository.loadStoryBookedById(dummyId)).thenReturn(expected)

        val actualStory: StoryFavoriteEntity =
            detailViewModel.loadStoryBookedById(dummyId).getOrAwaitValue()

        verify(storyRepository).loadStoryBookedById(dummyId)

        Assert.assertNull(actualStory)
    }

    @Test
    fun `when Insert Story to Bookmark`() = runTest {
        detailViewModel.insertStory(dummyStory)
        verify(storyRepository).insertStory(dummyStory)
    }

    @Test
    fun `when Delete Story from Bookmarked`() = runTest {
        detailViewModel.deleteStory(dummyId)
        verify(storyRepository).deleteStoryById(dummyId)
    }
}