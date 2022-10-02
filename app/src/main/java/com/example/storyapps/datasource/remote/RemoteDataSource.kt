package com.example.storyapps.datasource.remote

import com.example.storyapps.datasource.local.entity.LoginBody
import com.example.storyapps.datasource.local.entity.RegisterBody
import com.example.storyapps.datasource.remote.response.AddStoryResponse
import com.example.storyapps.datasource.remote.response.LoginResponse
import com.example.storyapps.datasource.remote.response.RegisterResponse
import com.example.storyapps.datasource.remote.response.StoryResponse
import com.example.storyapps.utils.EspressoIdlingResource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    fun registerAccount(
        registerBody: RegisterBody, callback: LoadRegisterAccountCallback
    ) {
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().registerAccount(registerBody)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onRegisterAccountReceived(ApiResponse.success(response.body() as RegisterResponse))
                } else {
                    callback.onRegisterAccountReceived(
                        ApiResponse.error(response.message(), response.body())
                    )
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback.onRegisterAccountReceived(ApiResponse.error(t.message.toString(), null))
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun loginAccount(loginBody: LoginBody, callback: LoadLoginAccountCallback) {
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().loginAccount(loginBody)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onLoginAccountReceived(ApiResponse.success(response.body() as LoginResponse))
                } else {
                    callback.onLoginAccountReceived(
                        ApiResponse.error(response.message(), response.body())
                    )
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onLoginAccountReceived(ApiResponse.error(t.message.toString(), null))
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun addStory(
        imageFile: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        token: String,
        callback: LoadAddStoryCallback
    ) {
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().addStory(imageFile, description, lat, lon, token)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>, response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    callback.onAddStoryReceived(ApiResponse.success(response.body() as AddStoryResponse))
                } else {
                    callback.onAddStoryReceived(
                        ApiResponse.error(response.message(), response.body())
                    )
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                callback.onAddStoryReceived(ApiResponse.error(t.message.toString(), null))
                EspressoIdlingResource.decrement()
            }
        })
    }

    suspend fun loadStory(
        size: Int, page: Int, header: String
    ): StoryResponse = ApiConfig.getApiService().loadStory(size, page, header)

    fun loadMapsStory(
        header: String, callback: LoadMapsStoryCallback
    ) {
        EspressoIdlingResource.increment()
        val client = ApiConfig.getApiService().loadMapsStory(1, header)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    callback.onMapsStoryReceived(ApiResponse.success(response.body() as StoryResponse))
                } else {
                    callback.onMapsStoryReceived(
                        ApiResponse.error(response.message(), response.body())
                    )
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                callback.onMapsStoryReceived(ApiResponse.error(t.message.toString(), null))
                EspressoIdlingResource.decrement()
            }
        })
    }

    interface LoadRegisterAccountCallback {
        fun onRegisterAccountReceived(registerAccount: ApiResponse<RegisterResponse>)
    }

    interface LoadLoginAccountCallback {
        fun onLoginAccountReceived(loginAccount: ApiResponse<LoginResponse>)
    }

    interface LoadAddStoryCallback {
        fun onAddStoryReceived(addStory: ApiResponse<AddStoryResponse>)
    }

    interface LoadMapsStoryCallback {
        fun onMapsStoryReceived(listStories: ApiResponse<StoryResponse>)
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource().apply { instance = this }
        }
    }
}