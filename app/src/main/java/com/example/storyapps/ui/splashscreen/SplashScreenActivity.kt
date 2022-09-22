package com.example.storyapps.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivitySplashScreenBinding
import com.example.storyapps.ui.home.HomeActivity
import com.example.storyapps.ui.login.LoginActivity
import com.example.storyapps.ui.viewmodel.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        splashScreenViewModel = obtainViewModel(this)
        splashScreenViewModel.getLoginStatus().observe(this) {
            val splashTime: Long = 2000
            Handler(Looper.getMainLooper()).postDelayed({
                if (it.isLogin) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, splashScreenBinding.ivSplashscreen, getString(
                        R.string.all_icon))
                    startActivity(intent, optionsCompat.toBundle())
                }
                finish()
            }, splashTime)
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): SplashScreenViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[SplashScreenViewModel::class.java]
    }
}