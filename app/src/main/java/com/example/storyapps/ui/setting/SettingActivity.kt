package com.example.storyapps.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.databinding.ActivitySettingBinding
import com.example.storyapps.ui.login.LoginActivity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import java.util.*

class SettingActivity : AppCompatActivity() {
    private lateinit var settingBinding: ActivitySettingBinding
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(settingBinding.root)
        settingViewModel = obtainViewModel(this)
        listener()
    }

    private fun listener(){
        with(settingBinding){
            imgSettingBack.setOnClickListener {
                onBackPressed()
            }
            tvSettingLogout.setOnClickListener {
                settingViewModel.logout()
                val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            btnLanguageSetting.text = Locale.getDefault().displayLanguage
            btnLanguageSetting.setOnClickListener {
                val intent = Intent(ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
    }
    private fun obtainViewModel(
        activity: AppCompatActivity
    ): SettingViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[SettingViewModel::class.java]
    }
}