package com.example.storyapps.ui.bookmarked

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapps.databinding.ActivityBookmarkedBinding
import com.example.storyapps.ui.viewmodel.ViewModelFactory

class BookmarkedActivity : AppCompatActivity() {
    private lateinit var bookmarkedBinding: ActivityBookmarkedBinding
    private lateinit var bookmarkedViewModel: BookmarkedViewModel
    private lateinit var bookmarkedAdapter: BookmarkedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookmarkedBinding = ActivityBookmarkedBinding.inflate(layoutInflater)
        setContentView(bookmarkedBinding.root)
        bookmarkedViewModel = obtainViewModel(this)
        bookmarkedAdapter = BookmarkedAdapter(this)
        with(bookmarkedBinding.rvBookmarked) {
            layoutManager = LinearLayoutManager(this@BookmarkedActivity)
            setHasFixedSize(true)
            adapter = bookmarkedAdapter
        }
        listener()
        checkData()
    }

    private fun listener() {
        with(bookmarkedBinding) {
            ivBookmarkedBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun checkData() {
        bookmarkedAdapter.addLoadStateListener {
            with(bookmarkedBinding) {
                if (bookmarkedAdapter.itemCount > 0) {
                    tvBookmarkedNofound.visibility = View.GONE
                    rvBookmarked.visibility = View.VISIBLE
                } else {
                    tvBookmarkedNofound.visibility = View.VISIBLE
                    rvBookmarked.visibility = View.GONE
                }
            }
        }
    }

    override fun onResume() {
        bookmarkedViewModel.loadStoriesBookmarked().observe(this@BookmarkedActivity) {
            bookmarkedAdapter.submitData(lifecycle, it)
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