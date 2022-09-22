package com.example.storyapps.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityHomeBinding
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.add.AddActivity
import com.example.storyapps.ui.add.AddActivity.Companion.EXTRA_ADD
import com.example.storyapps.ui.bookmarked.BookmarkedActivity
import com.example.storyapps.ui.setting.SettingActivity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import com.example.storyapps.utils.Utils.Companion.dataNotFound
import com.example.storyapps.utils.Utils.Companion.showLoading
import com.example.storyapps.vo.Status

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var storyAdapter: StoryAdapter
    private var page = 1
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        homeViewModel = obtainViewModel(this)
        storyAdapter = StoryAdapter()
        with(homeBinding.rvHome) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
        homeViewModel.getLoginStatus().observe(this) {
            token = "Bearer ${it.token}"
            loadData(token)
        }
        homeBinding.fabHomeAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(EXTRA_ADD, token)
            startActivity(intent)
        }
    }

    private fun loadData(token: String) {
        with(homeBinding) {
            showLoading(true, pbHome)
            homeViewModel.loadStory(page, token).observe(this@HomeActivity) {
                when (it.status) {
                    Status.LOADING -> showLoading(false, pbHome)
                    Status.SUCCESS -> {
                        showLoading(false, pbHome)
                        storyAdapter.setStory(it.data as List<StoryEntity>)
                    }
                    Status.ERROR -> {
                        showLoading(false, pbHome)
                        dataNotFound(true, tvHomeNofound)
                        if (it.message != null) {
                            Toast.makeText(this@HomeActivity, it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_bookmarked -> {
                val intent = Intent(this, BookmarkedActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HomeViewModel::class.java]
    }
}