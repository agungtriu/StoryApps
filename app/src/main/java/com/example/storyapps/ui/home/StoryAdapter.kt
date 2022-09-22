package com.example.storyapps.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapps.R
import com.example.storyapps.databinding.ListItemsBinding
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.helper.StoryDiffCallback
import com.example.storyapps.ui.detail.DetailActivity
import com.example.storyapps.ui.detail.DetailActivity.Companion.EXTRA_DETAIL

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    private val listStory = ArrayList<StoryEntity>()
    fun setStory(stories: List<StoryEntity>) {
        val diffCallback = StoryDiffCallback(listStory, stories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        if (listStory.isNotEmpty()) {
            listStory.clear()
        }
        listStory.addAll(stories)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: ListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryEntity) {
            with(binding) {
                Glide.with(itemView.context).load(story.photoUrl).into(ivItemPhoto)
                tvItemName.text = story.name
                tvItemDesc.text = story.description
                cvItem.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_DETAIL, story)
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity, Pair(
                            ivItemPhoto, itemView.context.getString(
                                R.string.photo
                            )
                        ), Pair(
                            tvItemName, itemView.context.getString(
                                R.string.all_name
                            )
                        ), Pair(
                            tvItemDesc, itemView.context.getString(
                                R.string.add_description
                            )
                        )
                    )
                    itemView.context.startActivity(
                        intent, optionsCompat.toBundle()
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = listStory.size
}