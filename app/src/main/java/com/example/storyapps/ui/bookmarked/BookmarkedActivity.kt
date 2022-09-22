package com.example.storyapps.ui.bookmarked

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapps.databinding.ActivityBookmarkedBinding
import com.example.storyapps.ui.home.StoryAdapter
import com.example.storyapps.ui.viewmodel.ViewModelFactory

class BookmarkedActivity : AppCompatActivity() {
    private lateinit var bookmarkedBinding: ActivityBookmarkedBinding
    private lateinit var bookmarkedViewModel: BookmarkedViewModel
    private lateinit var storyAdapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookmarkedBinding = ActivityBookmarkedBinding.inflate(layoutInflater)
        setContentView(bookmarkedBinding.root)
        bookmarkedViewModel = obtainViewModel(this)
        storyAdapter = StoryAdapter()
        with(bookmarkedBinding.rvBookmarked) {
            layoutManager = LinearLayoutManager(this@BookmarkedActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
        listener()
    }

    private fun listener() {
        with(bookmarkedBinding) {
            imgBookmarkedBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        with(bookmarkedBinding) {
            bookmarkedViewModel.loadStoriesBookmarked().observe(this@BookmarkedActivity){
                if (it.isNullOrEmpty()){
                    tvBookmarkedNofound.visibility = View.VISIBLE
                    rvBookmarked.visibility = View.GONE
                } else {
                    tvBookmarkedNofound.visibility = View.GONE
                    rvBookmarked.visibility = View.VISIBLE
                    storyAdapter.setStory(it)
                }
            }
        }
        super.onResume()
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): BookmarkedViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[BookmarkedViewModel::class.java]
    }
}