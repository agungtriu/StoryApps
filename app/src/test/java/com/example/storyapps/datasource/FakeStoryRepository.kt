package com.example.storyapps.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.storyapps.datasource.local.LocalDataSource
import com.example.storyapps.datasource.local.StoryDatabase
import com.example.storyapps.datasource.local.entity.*
import com.example.storyapps.datasource.remote.ApiResponse
import com.example.storyapps.datasource.remote.RemoteDataSource
import com.example.storyapps.datasource.remote.response.AddStoryResponse
import com.example.storyapps.datasource.remote.response.LoginResponse
import com.example.storyapps.datasource.remote.response.RegisterResponse
import com.example.storyapps.datasource.remote.response.StoryResponse
import com.example.storyapps.utils.Config
import com.example.storyapps.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoryRepository constructor(
    private val database: StoryDatabase,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : StoryDataSource {
    override fun registerAccount(
        registerBody: RegisterBody
    ): LiveData<Resource<RegisterEntity>> {
        val registerAccountResult = MutableLiveData<Resource<RegisterEntity>>()
        remoteDataSource.registerAccount(registerBody,
            object : RemoteDataSource.LoadRegisterAccountCallback {
                override fun onRegisterAccountReceived(registerAccount: ApiResponse<RegisterResponse>) {
                    if (registerAccount.body != null) {
                        val register = RegisterEntity(
                            registerAccount.body!!.error, registerAccount.body!!.message
                        )
                        registerAccountResult.postValue(Resource.success(register))
                    } else {
                        registerAccountResult.postValue(
                            Resource.error(
                                registerAccount.message, null
                            )
                        )
                    }
                }
            })
        return registerAccountResult
    }

    override fun loginAccount(
        loginBody: LoginBody
    ): LiveData<Resource<LoginEntity>> {
        val loginAccountResult = MutableLiveData<Resource<LoginEntity>>()
        remoteDataSource.loginAccount(loginBody,
            object : RemoteDataSource.LoadLoginAccountCallback {
                override fun onLoginAccountReceived(loginAccount: ApiResponse<LoginResponse>) {
                    if (loginAccount.body != null) {
                        if (loginAccount.body!!.loginResult != null) {
                            val login = LoginEntity(
                                loginAccount.body!!.loginResult!!.name,
                                loginAccount.body!!.loginResult!!.userId,
                                loginAccount.body!!.loginResult!!.token
                            )
                            loginAccountResult.postValue(Resource.success(login))
                        }
                    } else {
                        loginAccountResult.postValue(
                            Resource.error(
                                loginAccount.message, null
                            )
                        )
                    }
                }
            })
        return loginAccountResult
    }

    override fun addStory(
        imageFile: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        token: String
    ): LiveData<Resource<AddStoryEntity>> {
        val addStoryResult = MutableLiveData<Resource<AddStoryEntity>>()
        remoteDataSource.addStory(imageFile,
            description,
            lat,
            lon,
            token,
            object : RemoteDataSource.LoadAddStoryCallback {
                override fun onAddStoryReceived(addStory: ApiResponse<AddStoryResponse>) {
                    if (addStory.body != null) {
                        val add = AddStoryEntity(addStory.body!!.error, addStory.body!!.message)
                        addStoryResult.postValue(Resource.success(add))
                    } else {
                        addStoryResult.postValue(
                            Resource.error(
                                addStory.message, null
                            )
                        )
                    }
                }
            })
        return addStoryResult
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun loadStories(token: String): LiveData<PagingData<StoryEntity>> {
        return Pager(config = PagingConfig(
            pageSize = Config.PAGE_SIZE
        ), remoteMediator = StoryRemoteMediator(
            database, localDataSource, remoteDataSource, token
        ), pagingSourceFactory = {
            localDataSource.loadStories()
        }).liveData
    }

    override fun loadMapsStories(token: String): LiveData<Resource<List<StoryEntity>>> {
        val mapsStoryResult = MutableLiveData<Resource<List<StoryEntity>>>()
        remoteDataSource.loadMapsStory(token, object : RemoteDataSource.LoadMapsStoryCallback {
            override fun onMapsStoryReceived(listStories: ApiResponse<StoryResponse>) {
                if (listStories.body != null) {
                    val stories = arrayListOf<StoryEntity>()
                    if (listStories.body!!.listStory != null) {
                        for (i in listStories.body!!.listStory!!) {
                            if (i != null) {
                                if (i.id != null) {
                                    stories.add(
                                        StoryEntity(
                                            i.photoUrl,
                                            i.createdAt,
                                            i.name,
                                            i.description,
                                            i.lon,
                                            i.id!!,
                                            i.lat
                                        )
                                    )
                                }
                            }
                        }
                        mapsStoryResult.postValue(Resource.success(stories))
                    }
                } else {
                    mapsStoryResult.postValue(
                        Resource.error(
                            listStories.message, null
                        )
                    )
                }
            }
        })
        return mapsStoryResult
    }

    override fun loadStoriesBooked(): LiveData<PagingData<StoryFavoriteEntity>> {
        return Pager(config = PagingConfig(
            pageSize = Config.PAGE_SIZE
        ), pagingSourceFactory = {
            localDataSource.loadStoriesBooked()
        }).liveData
    }

    override fun loadStoryBookedById(id: String): LiveData<StoryFavoriteEntity> =
        localDataSource.loadStoryBookedById(id)

    override fun insertStory(storyFavoriteEntity: StoryFavoriteEntity) =
        localDataSource.insertStory(storyFavoriteEntity)

    override fun deleteStoryById(id: String) = localDataSource.deleteStoryById(id)
}