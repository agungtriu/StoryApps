package com.example.storyapps

import com.example.storyapps.datasource.local.entity.*
import com.example.storyapps.datasource.remote.response.AddStoryResponse
import com.example.storyapps.datasource.remote.response.LoginResponse
import com.example.storyapps.datasource.remote.response.RegisterResponse
import com.example.storyapps.datasource.remote.response.StoryResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Type

object DataDummy {
    private const val name = "berkah"
    private const val email = "berkah@domain.com"
    private const val password = "domain"
    const val token =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRjd3ZBcnFiZkw5U2FUbkQiLCJpYXQiOjE2NjQ0NDc3MTd9.IJ0ldF_D6R8pQashsgKKWA4nVwtPx8MaLDzc5D8Vny8"
    val registerRequest = RegisterBody(name, email, password)
    val loginRequest = LoginBody(email, password)

    fun loginResult(): LoginEntity {
        val json = """
            {
              "error": false,
              "message": "success",
              "loginResult": {
                "userId": "user-dcwvArqbfL9SaTnD",
                "name": "berkah",
                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRjd3ZBcnFiZkw5U2FUbkQiLCJpYXQiOjE2NjQ0NDc3MTd9.IJ0ldF_D6R8pQashsgKKWA4nVwtPx8MaLDzc5D8Vny8"
              }
            }
            """.trimIndent()
        return Gson().fromJson(json, LoginEntity::class.java)
    }

    fun loginResponse(): LoginResponse {
        val json = """
            {
              "error": false,
              "message": "success",
              "loginResult": {
                "userId": "user-dcwvArqbfL9SaTnD",
                "name": "berkah",
                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRjd3ZBcnFiZkw5U2FUbkQiLCJpYXQiOjE2NjQ0NDc3MTd9.IJ0ldF_D6R8pQashsgKKWA4nVwtPx8MaLDzc5D8Vny8"
              }
            }
            """.trimIndent()
        return Gson().fromJson(json, LoginResponse::class.java)
    }

    fun registerResult() = RegisterEntity(
        false, "User created"
    )

    fun registerResponse() = RegisterResponse(
        false, "User created"
    )

    fun addStoryResult() = AddStoryEntity(false, "success")

    fun addStoryResponse() = AddStoryResponse(false, "success")

    fun multipartFile() = MultipartBody.Part.create("dummyFile".toRequestBody())

    fun requestBody(text: String) = text.toRequestBody("text/plain".toMediaType())

    fun storyEntities(): List<StoryEntity> {
        val json = """
            [
                {
                  "id": "story-O8110bFn3StKHPxF",
                  "name": "tgh",
                  "description": "location",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664441542177_nrBchnyi.jpg",
                  "createdAt": "2022-09-29T08:52:22.179Z",
                  "lat": -6.2784307,
                  "lon": 106.8292903
                },
                {
                  "id": "story-HPubvmuMk5EHyq2k",
                  "name": "fyn",
                  "description": "Hello",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664433894717_bfJZX3D5.jpg",
                  "createdAt": "2022-09-29T06:44:54.719Z",
                  "lat": 37.421997,
                  "lon": -122.084
                },
                {
                  "id": "story-HDh4Vka7_TZJAKQ-",
                  "name": "modul1",
                  "description": "kmkkkk",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664417471667_0h4vUekD.jpg",
                  "createdAt": "2022-09-29T02:11:11.669Z",
                  "lat": -7.46343,
                  "lon": 112.4317533
                },
                {
                  "id": "story-aDkOoSfsXrL-AhA2",
                  "name": "fyn",
                  "description": "ss",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664415775695_efuFe-ON.jpg",
                  "createdAt": "2022-09-29T01:42:55.697Z",
                  "lat": 2,
                  "lon": 2
                },
                {
                  "id": "story-33c5PffzroMY4h5k",
                  "name": "berkah",
                  "description": "hmmm",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664386769067_KJqT-v1i.jpg",
                  "createdAt": "2022-09-28T17:39:29.068Z",
                  "lat": -7.7597684,
                  "lon": 110.4154974
                },
                {
                  "id": "story-QS4CY4F37XyNZIaa",
                  "name": "berkah",
                  "description": "hmmmm",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664384998291_tIAMXDt5.jpg",
                  "createdAt": "2022-09-28T17:09:58.295Z",
                  "lat": -7.7597706,
                  "lon": 110.4154896
                },
                {
                  "id": "story-pNW3TlPPDQg36Pky",
                  "name": "Rachmad Firdaus",
                  "description": "Bukan aku yang jebat, tapi do'a ibuku yang kuat",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664381480112_LwAlKKFY.image",
                  "createdAt": "2022-09-28T16:11:20.114Z",
                  "lat": -7.2354952,
                  "lon": 112.7695406
                },
                {
                  "id": "story--FVtPidZWZGThfyG",
                  "name": "Afif",
                  "description": "Hola",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664375842172_gRDXaVrn.jpg",
                  "createdAt": "2022-09-28T14:37:22.173Z",
                  "lat": -6.759222,
                  "lon": 110.75645
                },
                {
                  "id": "story-q2IZd9ph4jcsi5uh",
                  "name": "M Lukman Hakim",
                  "description": "again",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664371373812_BUtnEYhp.jpg",
                  "createdAt": "2022-09-28T13:22:53.813Z",
                  "lat": -8.769455,
                  "lon": 116.4022604
                },
                {
                  "id": "story-FF7CcQvusu8SCkNz",
                  "name": "M Lukman Hakim",
                  "description": "Hello",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664370999791_pvp2gLkx.jpg",
                  "createdAt": "2022-09-28T13:16:39.793Z",
                  "lat": -8.76945,
                  "lon": 116.40182
                }
            ]
        """.trimIndent()
        val listStory: Type = object : TypeToken<List<StoryEntity>>() {}.type
        return Gson().fromJson(json, listStory)
    }

    fun storiesResponse(): StoryResponse {
        val json = """
            {
                "error": false,
                "message": "Stories fetched successfully",
                "listStory": [
                        {
                          "id": "story-O8110bFn3StKHPxF",
                          "name": "tgh",
                          "description": "location",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664441542177_nrBchnyi.jpg",
                          "createdAt": "2022-09-29T08:52:22.179Z",
                          "lat": -6.2784307,
                          "lon": 106.8292903
                        },
                        {
                          "id": "story-HPubvmuMk5EHyq2k",
                          "name": "fyn",
                          "description": "Hello",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664433894717_bfJZX3D5.jpg",
                          "createdAt": "2022-09-29T06:44:54.719Z",
                          "lat": 37.421997,
                          "lon": -122.084
                        },
                        {
                          "id": "story-HDh4Vka7_TZJAKQ-",
                          "name": "modul1",
                          "description": "kmkkkk",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664417471667_0h4vUekD.jpg",
                          "createdAt": "2022-09-29T02:11:11.669Z",
                          "lat": -7.46343,
                          "lon": 112.4317533
                        },
                        {
                          "id": "story-aDkOoSfsXrL-AhA2",
                          "name": "fyn",
                          "description": "ss",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664415775695_efuFe-ON.jpg",
                          "createdAt": "2022-09-29T01:42:55.697Z",
                          "lat": 2,
                          "lon": 2
                        },
                        {
                          "id": "story-33c5PffzroMY4h5k",
                          "name": "berkah",
                          "description": "hmmm",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664386769067_KJqT-v1i.jpg",
                          "createdAt": "2022-09-28T17:39:29.068Z",
                          "lat": -7.7597684,
                          "lon": 110.4154974
                        },
                        {
                          "id": "story-QS4CY4F37XyNZIaa",
                          "name": "berkah",
                          "description": "hmmmm",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664384998291_tIAMXDt5.jpg",
                          "createdAt": "2022-09-28T17:09:58.295Z",
                          "lat": -7.7597706,
                          "lon": 110.4154896
                        },
                        {
                          "id": "story-pNW3TlPPDQg36Pky",
                          "name": "Rachmad Firdaus",
                          "description": "Bukan aku yang jebat, tapi do'a ibuku yang kuat",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664381480112_LwAlKKFY.image",
                          "createdAt": "2022-09-28T16:11:20.114Z",
                          "lat": -7.2354952,
                          "lon": 112.7695406
                        },
                        {
                          "id": "story--FVtPidZWZGThfyG",
                          "name": "Afif",
                          "description": "Hola",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664375842172_gRDXaVrn.jpg",
                          "createdAt": "2022-09-28T14:37:22.173Z",
                          "lat": -6.759222,
                          "lon": 110.75645
                        },
                        {
                          "id": "story-q2IZd9ph4jcsi5uh",
                          "name": "M Lukman Hakim",
                          "description": "again",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664371373812_BUtnEYhp.jpg",
                          "createdAt": "2022-09-28T13:22:53.813Z",
                          "lat": -8.769455,
                          "lon": 116.4022604
                        },
                        {
                          "id": "story-FF7CcQvusu8SCkNz",
                          "name": "M Lukman Hakim",
                          "description": "Hello",
                          "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664370999791_pvp2gLkx.jpg",
                          "createdAt": "2022-09-28T13:16:39.793Z",
                          "lat": -8.76945,
                          "lon": 116.40182
                        }
                ]
            }
        """.trimIndent()
        val listStory: Type = object : TypeToken<StoryResponse>() {}.type
        return Gson().fromJson(json, listStory)
    }

    fun storyFavoriteEntities(): List<StoryFavoriteEntity> {
        val json = """
            [
                {
                  "id": "story-O8110bFn3StKHPxF",
                  "name": "tgh",
                  "description": "location",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664441542177_nrBchnyi.jpg",
                  "createdAt": "2022-09-29T08:52:22.179Z",
                  "lat": -6.2784307,
                  "lon": 106.8292903
                },
                {
                  "id": "story-HPubvmuMk5EHyq2k",
                  "name": "fyn",
                  "description": "Hello",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664433894717_bfJZX3D5.jpg",
                  "createdAt": "2022-09-29T06:44:54.719Z",
                  "lat": 37.421997,
                  "lon": -122.084
                },
                {
                  "id": "story-HDh4Vka7_TZJAKQ-",
                  "name": "modul1",
                  "description": "kmkkkk",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664417471667_0h4vUekD.jpg",
                  "createdAt": "2022-09-29T02:11:11.669Z",
                  "lat": -7.46343,
                  "lon": 112.4317533
                },
                {
                  "id": "story-aDkOoSfsXrL-AhA2",
                  "name": "fyn",
                  "description": "ss",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664415775695_efuFe-ON.jpg",
                  "createdAt": "2022-09-29T01:42:55.697Z",
                  "lat": 2,
                  "lon": 2
                },
                {
                  "id": "story-33c5PffzroMY4h5k",
                  "name": "berkah",
                  "description": "hmmm",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664386769067_KJqT-v1i.jpg",
                  "createdAt": "2022-09-28T17:39:29.068Z",
                  "lat": -7.7597684,
                  "lon": 110.4154974
                },
                {
                  "id": "story-QS4CY4F37XyNZIaa",
                  "name": "berkah",
                  "description": "hmmmm",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664384998291_tIAMXDt5.jpg",
                  "createdAt": "2022-09-28T17:09:58.295Z",
                  "lat": -7.7597706,
                  "lon": 110.4154896
                },
                {
                  "id": "story-pNW3TlPPDQg36Pky",
                  "name": "Rachmad Firdaus",
                  "description": "Bukan aku yang jebat, tapi do'a ibuku yang kuat",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664381480112_LwAlKKFY.image",
                  "createdAt": "2022-09-28T16:11:20.114Z",
                  "lat": -7.2354952,
                  "lon": 112.7695406
                },
                {
                  "id": "story--FVtPidZWZGThfyG",
                  "name": "Afif",
                  "description": "Hola",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664375842172_gRDXaVrn.jpg",
                  "createdAt": "2022-09-28T14:37:22.173Z",
                  "lat": -6.759222,
                  "lon": 110.75645
                },
                {
                  "id": "story-q2IZd9ph4jcsi5uh",
                  "name": "M Lukman Hakim",
                  "description": "again",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664371373812_BUtnEYhp.jpg",
                  "createdAt": "2022-09-28T13:22:53.813Z",
                  "lat": -8.769455,
                  "lon": 116.4022604
                },
                {
                  "id": "story-FF7CcQvusu8SCkNz",
                  "name": "M Lukman Hakim",
                  "description": "Hello",
                  "photoUrl": "https://story-api.dicoding.dev/images/stories/photos-1664370999791_pvp2gLkx.jpg",
                  "createdAt": "2022-09-28T13:16:39.793Z",
                  "lat": -8.76945,
                  "lon": 116.40182
                }
            ]
        """.trimIndent()
        val listStory: Type = object : TypeToken<List<StoryFavoriteEntity>>() {}.type
        return Gson().fromJson(json, listStory)
    }
}