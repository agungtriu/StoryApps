package com.example.storyapps.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.storyapps.R
import com.example.storyapps.utils.Config.Companion.BASE_URL
import com.example.storyapps.utils.EspressoIdlingResource
import com.example.storyapps.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

@MediumTest
class HomeActivityTest {
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        BASE_URL = "http://127.0.0.1:8080"
        IdlingRegistry.getInstance()
            .register(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance()
            .unregister(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
    }

    @Test
    fun loadStories_Success() {
        ActivityScenario.launch(HomeActivity::class.java)

        val mockResponse = MockResponse().setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withText("tgh")).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                hasDescendant(withText("berkah"))
            )
        )
    }
}