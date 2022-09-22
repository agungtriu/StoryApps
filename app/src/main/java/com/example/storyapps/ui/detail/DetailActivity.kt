package com.example.storyapps.ui.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityDetailBinding
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import com.example.storyapps.utils.Utils.Companion.formatDate
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailEntity: StoryEntity
    private var state: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val detailStory = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL, StoryEntity::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_DETAIL)
        }

        if (detailStory != null) {
            detailEntity = detailStory
        }
        with(detailBinding) {
            if (detailStory != null) {
                Glide.with(this@DetailActivity).load(detailStory.photoUrl).into(ivDetailPhoto)
                tvDetailName.text = detailStory.name
                tvDetailDate.text =
                    formatDate(detailStory.createdAt.toString(), TimeZone.getDefault().id)
                tvDetailDescription.text = detailStory.description
            }
        }
        detailViewModel = obtainViewModel(this)
        loadStateBookmark(detailEntity.id)
        listener()
    }

    private fun listener() {
        with(detailBinding) {
            imgDetailBookmark.setOnClickListener {
                if (state) {
                    detailViewModel.deleteStory(detailEntity.id)
                } else {
                    detailViewModel.insertStory(detailEntity)
                }
                loadStateBookmark(detailEntity.id)
            }
            imgDetailBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun loadStateBookmark(id: String) {
        detailViewModel.loadStoryBookedById(id).observe(this) {
            if (it != null) {
                state = true
                detailBinding.imgDetailBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity, R.drawable.ic_bookmarked
                    )
                )
            } else {
                state = false
                detailBinding.imgDetailBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity, R.drawable.ic_bookmark
                    )
                )
            }
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}