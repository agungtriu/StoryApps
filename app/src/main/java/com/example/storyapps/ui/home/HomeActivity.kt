package com.example.storyapps.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapps.databinding.ActivityHomeBinding
import com.example.storyapps.ui.add.AddActivity
import com.example.storyapps.ui.add.AddActivity.Companion.EXTRA_ADD
import com.example.storyapps.ui.bookmarked.BookmarkedActivity
import com.example.storyapps.ui.maps.MapsActivity
import com.example.storyapps.ui.setting.SettingActivity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import com.example.storyapps.utils.Utils.Companion.showLoading

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var storyAdapter: StoryAdapter
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        homeViewModel = obtainViewModel(this)
        storyAdapter = StoryAdapter(this)
        with(homeBinding.rvHome) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
        homeViewModel.getLoginStatus().observe(this) {
            token = "Bearer ${it.token}"
            loadData(token)
        }
        checkData()
        listener()
    }

    private fun checkData() {
        storyAdapter.addLoadStateListener {
            with(homeBinding) {
                if (storyAdapter.itemCount > 0) {
                    tvHomeEmpty.visibility = View.GONE
                    rvHome.visibility = View.VISIBLE
                } else {
                    tvHomeEmpty.visibility = View.VISIBLE
                    rvHome.visibility = View.GONE
                }
            }
        }
    }

    private fun listener(){
        with(homeBinding){
            ivHomeMaps.setOnClickListener {
                val intent = Intent(this@HomeActivity, MapsActivity::class.java)
                startActivity(intent)
            }
            ivHomeBookmarked.setOnClickListener {
                val intent = Intent(this@HomeActivity, BookmarkedActivity::class.java)
                startActivity(intent)
            }
            ivHomeSetting.setOnClickListener {
                val intent = Intent(this@HomeActivity, SettingActivity::class.java)
                startActivity(intent)
            }
            fabHomeAdd.setOnClickListener {
                val intent = Intent(this@HomeActivity, AddActivity::class.java)
                intent.putExtra(EXTRA_ADD, token)
                startActivity(intent)
            }
        }
    }

    private fun loadData(token: String) {
        with(homeBinding) {
            pbHome.showLoading(true)
            homeViewModel.loadStory(token).observe(this@HomeActivity) {
                pbHome.showLoading(false)
                storyAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HomeViewModel::class.java]
    }
}