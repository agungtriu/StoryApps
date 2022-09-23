package com.example.storyapps.datasource.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapps.datasource.local.entity.LoginBody
import com.example.storyapps.datasource.local.entity.RegisterBody
import com.example.storyapps.datasource.remote.response.AddStoryResponse
import com.example.storyapps.datasource.remote.response.LoginResponse
import com.example.storyapps.datasource.remote.response.RegisterResponse
import com.example.storyapps.datasource.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    fun registerAccount(
        registerBody: RegisterBody, callback: LoadRegisterAccountCallback
    ) {
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
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                callback.onRegisterAccountReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
    }

    fun loginAccount(loginBody: LoginBody, callback: LoadLoginAccountCallback) {
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
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onLoginAccountReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
    }

    fun addStory(
        imageFile: MultipartBody.Part,
        description: RequestBody,
        token: String,
        callback: LoadAddStoryCallback
    ) {
        val client = ApiConfig.getApiService().addStory(imageFile, description, token)
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
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                callback.onAddStoryReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
    }

    fun loadStory(
        page: Int, header: String, callback: LoadAllStoryCallback
    ): LiveData<ApiResponse<StoryResponse>> {
        val resultStories = MutableLiveData<ApiResponse<StoryResponse>>()
        val client = ApiConfig.getApiService().loadStory(page, header)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    callback.onAllStoryReceived(ApiResponse.success(response.body() as StoryResponse))
                } else {
                    callback.onAllStoryReceived(
                        ApiResponse.error(
                            response.message(), response.body()
                        )
                    )
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                callback.onAllStoryReceived(ApiResponse.error(t.message.toString(), null))
            }
        })
        return resultStories
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

    interface LoadAllStoryCallback {
        fun onAllStoryReceived(listStories: ApiResponse<StoryResponse>)
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource().apply { instance = this }
        }
    }
}