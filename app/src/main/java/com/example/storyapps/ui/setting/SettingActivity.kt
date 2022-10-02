package com.example.storyapps.ui.setting

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.R
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

    private fun listener() {
        with(settingBinding) {
            ivSettingBack.setOnClickListener {
                onBackPressed()
            }
            tvSettingLogout.setOnClickListener {
                dialogLogout()
            }
            tvLanguageSetting.text = Locale.getDefault().displayLanguage
            tvLanguageSetting.setOnClickListener {
                val intent = Intent(ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun dialogLogout() {
        val builder = Dialog(this)
        with(builder) {
            setContentView(R.layout.dialog_logout)
            findViewById<TextView>(R.id.tv_logout_cancel).setOnClickListener {
                dismiss()
            }
            findViewById<TextView>(R.id.tv_logout_confirm).setOnClickListener {
                settingViewModel.logout()
                val intent = Intent(this@SettingActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dismiss()
            }
            show()
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): SettingViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[SettingViewModel::class.java]
    }
}