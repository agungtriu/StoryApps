package com.example.storyapps.ui.splashscreen

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.example.storyapps.R
import com.example.storyapps.utils.EspressoIdlingResource
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runners.MethodSorters


@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SplashScreenActivityTest {
    private val name = "endtoendtestingcfvgbh"
    private val email = "endtoendtestingvgbh@gmail.com"
    private val password = "123456"
    private val caption = "end to end testing"

    @get:Rule
    var grantPermissions: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Before
    fun setUp() {
        ActivityScenario.launch(SplashScreenActivity::class.java)
        IdlingRegistry.getInstance()
            .register(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())

    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance()
            .unregister(EspressoIdlingResource.getEspressoIdlingResourceForMainActivity())
    }

    @Test
    fun step01_RegisterAccount_Success() {
        onView(withId(R.id.bt_login_register)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_login_register)).perform(click())
        onView(withId(R.id.ed_register_name)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_register_name)).perform(typeText(name), closeSoftKeyboard())
        onView(withId(R.id.ed_register_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_register_email)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ed_register_password)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_register_password)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.bt_register)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_register)).perform(click())
    }

    @Test
    fun step02_LoginAccount_Success() {
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_email)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.bt_login)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_login)).perform(click())
    }

    @Test
    fun step03_LoadStory_Success() {
        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(swipeUp())
    }

    @Test
    fun step04_LoadDetailStory_Success() {
        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            )
        )
        onView(withId(R.id.iv_detail_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(CoreMatchers.not(ViewMatchers.withText(""))))
        onView(withId(R.id.tv_detail_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_date)).check(matches(CoreMatchers.not(ViewMatchers.withText(""))))
        onView(withId(R.id.tv_detail_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_description)).check(
            matches(
                CoreMatchers.not(
                    ViewMatchers.withText(
                        ""
                    )
                )
            )
        )
    }

    @Test
    fun step05_InsertBookmarkStory_Success() {
        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            )
        )
        onView(withId(R.id.iv_detail_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(CoreMatchers.not(ViewMatchers.withText(""))))
        onView(withId(R.id.tv_detail_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_date)).check(matches(CoreMatchers.not(ViewMatchers.withText(""))))
        onView(withId(R.id.tv_detail_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_description)).check(
            matches(
                CoreMatchers.not(
                    ViewMatchers.withText(
                        ""
                    )
                )
            )
        )
        onView(withId(R.id.iv_detail_bookmark)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_bookmark)).perform(click())
        onView(withId(R.id.iv_detail_back)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_back)).perform(click())
        onView(withId(R.id.iv_home_bookmarked)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_home_bookmarked)).perform(click())
        onView(withId(R.id.rv_bookmarked)).check(matches(isDisplayed()))
    }

    @Test
    fun step06_DeleteBookmarkedStory_Success() {
        onView(withId(R.id.iv_home_bookmarked)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_home_bookmarked)).perform(click())
        onView(withId(R.id.rv_bookmarked)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_bookmarked)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()
            )
        )
        onView(withId(R.id.iv_detail_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(CoreMatchers.not(ViewMatchers.withText(""))))
        onView(withId(R.id.tv_detail_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_date)).check(matches(CoreMatchers.not(ViewMatchers.withText(""))))
        onView(withId(R.id.tv_detail_description)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_description)).check(
            matches(
                CoreMatchers.not(
                    ViewMatchers.withText(
                        ""
                    )
                )
            )
        )
        onView(withId(R.id.iv_detail_bookmark)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_bookmark)).perform(click())
        onView(withId(R.id.iv_detail_back)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_back)).perform(click())
    }

    @Test
    fun step07_AddStoryCameraX_Success() {
        onView(withId(R.id.fab_home_add)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_home_add)).perform(click())
        onView(withId(R.id.cv_add)).check(matches(isDisplayed()))
        onView(withId(R.id.cv_add)).perform(click())
        onView(withId(R.id.cl_resources_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.cl_resources_camera)).perform(click())
        onView(withId(R.id.pv_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_camera_capture)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_camera_capture)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.iv_add_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).perform(
            typeText(caption), closeSoftKeyboard()
        )
        onView(withId(R.id.bt_add)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_add)).perform(click())
        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(swipeDown())
    }

    @Test
    fun step08_AddStoryCameraXSelfie_Success() {
        onView(withId(R.id.fab_home_add)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_home_add)).perform(click())
        onView(withId(R.id.cv_add)).check(matches(isDisplayed()))
        onView(withId(R.id.cv_add)).perform(click())
        onView(withId(R.id.cl_resources_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.cl_resources_camera)).perform(click())
        onView(withId(R.id.pv_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_camera_switch)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_camera_switch)).perform(click())
        onView(withId(R.id.iv_camera_capture)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_camera_capture)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.iv_add_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).perform(
            typeText(caption), closeSoftKeyboard()
        )
        onView(withId(R.id.bt_add)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_add)).perform(click())
        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(swipeDown())
    }

    @Test
    fun step09_AddStoryGallery_Success() {
        onView(withId(R.id.fab_home_add)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_home_add)).perform(click())
        onView(withId(R.id.cv_add)).check(matches(isDisplayed()))
        onView(withId(R.id.cv_add)).perform(click())
        onView(withId(R.id.cl_resources_gallery)).check(matches(isDisplayed()))
        Intents.init()
        val expectedRespond = createGalleryPickActivityResultStub()
        Intents.intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(expectedRespond)
        onView(withId(R.id.cl_resources_gallery)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.iv_add_preview)).check(matches(isDisplayed()))
        Intents.release()
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).perform(
            typeText(caption), closeSoftKeyboard()
        )
        onView(withId(R.id.bt_add)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_add)).perform(click())
        onView(withId(R.id.rv_home)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_home)).perform(swipeDown())

    }

    private fun createGalleryPickActivityResultStub(): Instrumentation.ActivityResult {
        val resources: Resources = getInstrumentation().context.resources
        val imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(R.drawable.ic_placeholder) + '/' + resources.getResourceTypeName(
                R.drawable.ic_placeholder
            ) + '/' + resources.getResourceEntryName(R.drawable.ic_placeholder)
        )
        val resultIntent = Intent()
        resultIntent.data = imageUri
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)
    }

    @Test
    fun step10_LoadMaps_Success() {
        onView(withId(R.id.iv_home_maps)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_home_maps)).perform(click())
        onView(withId(R.id.f_maps)).check(matches(isDisplayed()))
    }

    @Test
    fun step11_Logout_Cancel() {
        onView(withId(R.id.iv_home_setting)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_home_setting)).perform(click())
        onView(withId(R.id.tv_setting_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_setting_logout)).perform(click())
        onView(withId(R.id.tv_logout_cancel)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_logout_cancel)).perform(click())
        onView(withId(R.id.tv_setting_logout)).check(matches(isDisplayed()))
    }

    @Test
    fun step12_Logout_Success() {
        onView(withId(R.id.iv_home_setting)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_home_setting)).perform(click())
        onView(withId(R.id.tv_setting_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_setting_logout)).perform(click())
        onView(withId(R.id.tv_logout_confirm)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_logout_confirm)).perform(click())
        onView(withId(R.id.tv_login_title)).check(matches(isDisplayed()))
    }
}