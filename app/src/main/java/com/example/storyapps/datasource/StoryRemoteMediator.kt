package com.example.storyapps.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapps.datasource.local.LocalDataSource
import com.example.storyapps.datasource.local.StoryDatabase
import com.example.storyapps.datasource.local.entity.RemoteKeys
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.datasource.remote.RemoteDataSource
import com.example.storyapps.utils.Config.Companion.PAGE_SIZE

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val token: String
) : RemoteMediator<Int, StoryEntity>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        return try {
            val responseData = arrayListOf<StoryEntity>()
            if (remoteDataSource.loadStory(size = PAGE_SIZE, page, token).listStory != null) {
                val stories = remoteDataSource.loadStory(size = PAGE_SIZE, page, token).listStory
                if (stories != null) {
                    for (story in stories) {
                        if (story != null) {
                            responseData.add(
                                StoryEntity(
                                    story.photoUrl,
                                    story.createdAt,
                                    story.name,
                                    story.description,
                                    story.lon,
                                    story.id.toString(),
                                    story.lat
                                )
                            )
                        }
                    }
                }
            }
            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteRemoteKeys()
                    localDataSource.deleteAll()
                }
                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = (responseData).map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                localDataSource.insertRemoteAll(keys)
                localDataSource.insertStories(responseData)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            localDataSource.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                localDataSource.getRemoteKeysId(id)
            }
        }
    }

}