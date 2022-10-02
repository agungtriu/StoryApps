package com.example.storyapps.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.storyapps.DataDummy
import com.example.storyapps.LiveDataTestUtil.getOrAwaitValue
import com.example.storyapps.MainDispatcherRule
import com.example.storyapps.StoryFavoritePagingSource
import com.example.storyapps.StoryPagingSource
import com.example.storyapps.Utils.Companion.noopListUpdateCallback
import com.example.storyapps.datasource.local.LocalDataSource
import com.example.storyapps.datasource.local.StoryDatabase
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity
import com.example.storyapps.datasource.remote.ApiResponse
import com.example.storyapps.datasource.remote.RemoteDataSource
import com.example.storyapps.ui.bookmarked.BookmarkedAdapter
import com.example.storyapps.ui.home.StoryAdapter
import com.example.storyapps.vo.Status
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    private lateinit var storyRepository: FakeStoryRepository

    @Mock
    private lateinit var storyRepositoryMock: StoryRepository

    @Mock
    private lateinit var database: StoryDatabase

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    private val dummyRegisterRequest = DataDummy.registerRequest
    private val dummyRegisterResult = DataDummy.registerResponse()

    private val dummyLoginRequest = DataDummy.loginRequest
    private val dummyLoginResult = DataDummy.loginResponse()

    private val dummyAddStoryResponse = DataDummy.addStoryResponse()
    private val dummyMultiPartRequest = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.requestBody("Description")
    private val dummyLat = DataDummy.requestBody("2")
    private val dummyLon = DataDummy.requestBody("2")
    private val dummyToken = DataDummy.token

    private val dummyMapResult = DataDummy.storiesResponse()

    private val dummyStoryBookmarkedResult = DataDummy.storyFavoriteEntities()

    private val dummyStoryResult = DataDummy.storyEntities()


    @Before
    fun setUp() {
        storyRepository = FakeStoryRepository(database, remoteDataSource, localDataSource)
    }

    @Test
    fun `when Register Account Successfully`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadRegisterAccountCallback).onRegisterAccountReceived(
                ApiResponse.success(dummyRegisterResult)
            )
            null
        }.`when`(remoteDataSource).registerAccount(eq(dummyRegisterRequest), any())

        val registerResult = storyRepository.registerAccount(dummyRegisterRequest).getOrAwaitValue()
        verify(remoteDataSource).registerAccount(eq(dummyRegisterRequest), any())
        assertNotNull(registerResult.data)
        assertTrue(registerResult.status == Status.SUCCESS)
    }

    @Test
    fun `when Register Account Failed`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadRegisterAccountCallback).onRegisterAccountReceived(
                ApiResponse.error("", null)
            )
            null
        }.`when`(remoteDataSource).registerAccount(eq(dummyRegisterRequest), any())

        val registerResult = storyRepository.registerAccount(dummyRegisterRequest).getOrAwaitValue()
        verify(remoteDataSource).registerAccount(eq(dummyRegisterRequest), any())
        assertNull(registerResult.data)
        assertTrue(registerResult.status == Status.ERROR)
    }

    @Test
    fun `when Login Successfully`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadLoginAccountCallback).onLoginAccountReceived(
                ApiResponse.success(dummyLoginResult)
            )
            null
        }.`when`(remoteDataSource).loginAccount(eq(dummyLoginRequest), any())

        val loginResult = storyRepository.loginAccount(dummyLoginRequest).getOrAwaitValue()
        verify(remoteDataSource).loginAccount(eq(dummyLoginRequest), any())
        assertNotNull(loginResult.data)
        assertTrue(loginResult.status == Status.SUCCESS)
    }

    @Test
    fun `when Login Failed`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadLoginAccountCallback).onLoginAccountReceived(
                ApiResponse.error("", null)
            )
            null
        }.`when`(remoteDataSource).loginAccount(eq(dummyLoginRequest), any())

        val loginResult = storyRepository.loginAccount(dummyLoginRequest).getOrAwaitValue()
        verify(remoteDataSource).loginAccount(eq(dummyLoginRequest), any())
        assertNull(loginResult.data)
        assertTrue(loginResult.status == Status.ERROR)
    }

    @Test
    fun `when AddStory Successfully`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[5] as RemoteDataSource.LoadAddStoryCallback).onAddStoryReceived(
                ApiResponse.success(dummyAddStoryResponse)
            )
            null
        }.`when`(remoteDataSource).addStory(
            eq(dummyMultiPartRequest),
            eq(dummyDescription),
            eq(dummyLat),
            eq(dummyLon),
            eq(dummyToken),
            any()
        )

        val addStoryResult = storyRepository.addStory(
            dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
        ).getOrAwaitValue()
        verify(remoteDataSource).addStory(
            eq(dummyMultiPartRequest),
            eq(dummyDescription),
            eq(dummyLat),
            eq(dummyLon),
            eq(dummyToken),
            any()
        )
        assertNotNull(addStoryResult.data)
        assertTrue(addStoryResult.status == Status.SUCCESS)
    }

    @Test
    fun `when AddStory Failed`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[5] as RemoteDataSource.LoadAddStoryCallback).onAddStoryReceived(
                ApiResponse.error("", null)
            )
            null
        }.`when`(remoteDataSource).addStory(
            eq(dummyMultiPartRequest),
            eq(dummyDescription),
            eq(dummyLat),
            eq(dummyLon),
            eq(dummyToken),
            any()
        )

        val addStoryResult = storyRepository.addStory(
            dummyMultiPartRequest, dummyDescription, dummyLat, dummyLon, dummyToken
        ).getOrAwaitValue()
        verify(remoteDataSource).addStory(
            eq(dummyMultiPartRequest),
            eq(dummyDescription),
            eq(dummyLat),
            eq(dummyLon),
            eq(dummyToken),
            any()
        )
        assertNull(addStoryResult.data)
        assertTrue(addStoryResult.status == Status.ERROR)
    }

    @Test
    fun `when Load Story paging`() = runTest {
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStoryResult)
        val expected = MutableLiveData<PagingData<StoryEntity>>()
        expected.value = data

        `when`(storyRepositoryMock.loadStories(dummyToken)).thenReturn(expected)

        val actualStory: PagingData<StoryEntity> =
            storyRepositoryMock.loadStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStoryResult, differ.snapshot())
        assertEquals(dummyStoryResult.size, differ.snapshot().size)
        assertEquals(dummyStoryResult[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `when Load Maps Successfully`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMapsStoryCallback).onMapsStoryReceived(
                ApiResponse.success(dummyMapResult)
            )
            null
        }.`when`(remoteDataSource).loadMapsStory(
            eq(dummyToken), any()
        )

        val loadMapsResult = storyRepository.loadMapsStories(
            dummyToken
        ).getOrAwaitValue()
        verify(remoteDataSource).loadMapsStory(
            eq(dummyToken), any()
        )
        assertNotNull(loadMapsResult.data)
        assertTrue(loadMapsResult.status == Status.SUCCESS)
    }

    @Test
    fun `when Load Maps Failed`() = runTest {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMapsStoryCallback).onMapsStoryReceived(
                ApiResponse.error("", null)
            )
            null
        }.`when`(remoteDataSource).loadMapsStory(
            eq(dummyToken), any()
        )

        val loadMapsResult = storyRepository.loadMapsStories(
            dummyToken
        ).getOrAwaitValue()
        verify(remoteDataSource).loadMapsStory(
            eq(dummyToken), any()
        )
        assertNull(loadMapsResult.data)
        assertTrue(loadMapsResult.status == Status.ERROR)
    }

    @Test
    fun `when Load Story Bookmarked paging`() = runTest {
        val data: PagingData<StoryFavoriteEntity> =
            StoryFavoritePagingSource.snapshot(dummyStoryBookmarkedResult)
        val expected = MutableLiveData<PagingData<StoryFavoriteEntity>>()
        expected.value = data

        `when`(storyRepositoryMock.loadStoriesBooked()).thenReturn(expected)

        val actualStory: PagingData<StoryFavoriteEntity> =
            storyRepositoryMock.loadStoriesBooked().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = BookmarkedAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStoryBookmarkedResult, differ.snapshot())
        assertEquals(dummyStoryBookmarkedResult.size, differ.snapshot().size)
        assertEquals(dummyStoryBookmarkedResult[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `when Load Story by Id`() = runTest {
        val expected = MutableLiveData<StoryFavoriteEntity>()
        expected.value = dummyStoryBookmarkedResult[0]
        `when`(localDataSource.loadStoryBookedById(dummyStoryBookmarkedResult[0].id)).thenReturn(
            expected
        )

        val storyBookmarkedResult =
            storyRepository.loadStoryBookedById(dummyStoryBookmarkedResult[0].id).getOrAwaitValue()
        verify(localDataSource).loadStoryBookedById(dummyStoryBookmarkedResult[0].id)
        assertNotNull(storyBookmarkedResult)
        assertEquals(dummyStoryBookmarkedResult[0].id, storyBookmarkedResult.id)
        assertEquals(dummyStoryBookmarkedResult[0].name, storyBookmarkedResult.name)
        assertEquals(dummyStoryBookmarkedResult[0].photoUrl, storyBookmarkedResult.photoUrl)
        assertEquals(dummyStoryBookmarkedResult[0].createdAt, storyBookmarkedResult.createdAt)
        assertEquals(
            dummyStoryBookmarkedResult[0].description, storyBookmarkedResult.description
        )
        assertEquals(dummyStoryBookmarkedResult[0].lat, storyBookmarkedResult.lat)
        assertEquals(dummyStoryBookmarkedResult[0].lon, storyBookmarkedResult.lon)
    }

    @Test
    fun `when Insert Story to Bookmark`() {
        storyRepository.insertStory(dummyStoryBookmarkedResult[0])
        verify(localDataSource).insertStory(dummyStoryBookmarkedResult[0])
        verifyNoMoreInteractions(localDataSource)
    }

    @Test
    fun `when Delete Story from Bookmark`() {
        storyRepository.deleteStoryById(dummyStoryBookmarkedResult[0].id)
        verify(localDataSource).deleteStoryById(dummyStoryBookmarkedResult[0].id)
        verifyNoMoreInteractions(localDataSource)
    }
}