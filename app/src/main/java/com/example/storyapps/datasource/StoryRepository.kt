package com.example.storyapps.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapps.datasource.local.LocalDataSource
import com.example.storyapps.datasource.local.entity.*
import com.example.storyapps.datasource.remote.ApiResponse
import com.example.storyapps.datasource.remote.RemoteDataSource
import com.example.storyapps.datasource.remote.response.AddStoryResponse
import com.example.storyapps.datasource.remote.response.LoginResponse
import com.example.storyapps.datasource.remote.response.RegisterResponse
import com.example.storyapps.datasource.remote.response.StoryResponse
import com.example.storyapps.utils.AppExecutors
import com.example.storyapps.vo.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : StoryDataSource {
    override fun registerAccount(
        registerBody: RegisterBody
    ): LiveData<Resource<RegisterEntity>> {
        val registerAccountResult = MutableLiveData<Resource<RegisterEntity>>()
        remoteDataSource.registerAccount(
            registerBody,
            object : RemoteDataSource.LoadRegisterAccountCallback {
                override fun onRegisterAccountReceived(registerAccount: ApiResponse<RegisterResponse>) {
                    if (registerAccount.body != null) {
                        val register =
                            RegisterEntity(registerAccount.body.error, registerAccount.body.message)
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
        remoteDataSource.loginAccount(
            loginBody,
            object : RemoteDataSource.LoadLoginAccountCallback {
                override fun onLoginAccountReceived(loginAccount: ApiResponse<LoginResponse>) {
                    if (loginAccount.body != null) {
                        if (loginAccount.body.loginResult != null) {
                            val login = LoginEntity(
                                loginAccount.body.loginResult.name,
                                loginAccount.body.loginResult.userId,
                                loginAccount.body.loginResult.token
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
        imageFile: MultipartBody.Part, description: RequestBody, token: String
    ): LiveData<Resource<AddStoryEntity>> {
        val addStoryResult = MutableLiveData<Resource<AddStoryEntity>>()
        remoteDataSource.addStory(
            imageFile,
            description,
            token,
            object : RemoteDataSource.LoadAddStoryCallback {
                override fun onAddStoryReceived(addStory: ApiResponse<AddStoryResponse>) {
                    if (addStory.body != null) {
                        val add = AddStoryEntity(addStory.body.error, addStory.body.message)
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

    override fun loadStories(page: Int, token: String): LiveData<Resource<List<StoryEntity>>> {
        val storiesResult = MutableLiveData<Resource<List<StoryEntity>>>()
        remoteDataSource.loadStory(page, token, object : RemoteDataSource.LoadAllStoryCallback {
            override fun onAllStoryReceived(listStories: ApiResponse<StoryResponse>) {
                if (listStories.body != null) {
                    val storyList = ArrayList<StoryEntity>()
                    if (listStories.body.listStory != null) {
                        for (response in listStories.body.listStory) {
                            if (response != null) {
                                if (response.id != null) {
                                    val story = StoryEntity(
                                        response.photoUrl,
                                        response.createdAt,
                                        response.name,
                                        response.description,
                                        response.lon,
                                        response.id,
                                        response.lat
                                    )
                                    storyList.add(story)
                                }
                            }
                        }
                        storiesResult.postValue(Resource.success(storyList))
                    } else {
                        storiesResult.postValue(
                            Resource.error(
                                listStories.message, null
                            )
                        )
                    }
                } else {
                    storiesResult.postValue(
                        Resource.error(
                            listStories.message, null
                        )
                    )
                }
            }
        })
        return storiesResult
    }

    override fun loadStoriesBooked(): LiveData<List<StoryEntity>> = localDataSource.loadStoriesBooked()

    override fun loadStoryBookedById(id: String): LiveData<StoryEntity> = localDataSource.loadStoryBookedById(id)

    override fun insertStory(storyEntity: StoryEntity) =
        appExecutors.diskIO().execute { localDataSource.insertStory(storyEntity) }

    override fun deleteStoryById(id: String) =
        appExecutors.diskIO().execute { localDataSource.deleteStoryById(id) }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            remoteData: RemoteDataSource, localData: LocalDataSource, appExecutors: AppExecutors
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(
                remoteData, localData, appExecutors
            ).apply { instance = this }
        }
    }
}