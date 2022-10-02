package com.example.storyapps.ui.bookmarked

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.StoryFavoritePagingSource
import com.example.storyapps.Utils.Companion.noopListUpdateCallback
import com.example.storyapps.datasource.StoryRepository
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BookmarkedViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var bookmarkedViewModel: BookmarkedViewModel
    private val dummyStory = DataDummy.storyFavoriteEntities()

    @Before
    fun setUp() {
        bookmarkedViewModel = BookmarkedViewModel(storyRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Get Stories Bookmarked Should Not Null`() = runTest {
        val data: PagingData<StoryFavoriteEntity> = StoryFavoritePagingSource.snapshot(dummyStory)
        val expected = MutableLiveData<PagingData<StoryFavoriteEntity>>()
        expected.value = data

        `when`(storyRepository.loadStoriesBooked()).thenReturn(expected)

        val actualStory: PagingData<StoryFavoriteEntity> =
            bookmarkedViewModel.loadStoriesBookmarked().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = BookmarkedAdapter.DIFF_CALLBACK,
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