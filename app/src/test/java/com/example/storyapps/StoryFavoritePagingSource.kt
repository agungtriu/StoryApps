package com.example.storyapps

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity

class StoryFavoritePagingSource : PagingSource<Int, LiveData<List<StoryFavoriteEntity>>>() {
    companion object {
        fun snapshot(items: List<StoryFavoriteEntity>): PagingData<StoryFavoriteEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryFavoriteEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryFavoriteEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}