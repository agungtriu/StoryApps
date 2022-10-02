package com.example.storyapps.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.storyapps.R
import com.example.storyapps.databinding.ListItemsBinding
import com.example.storyapps.datasource.local.entity.StoryEntity
import com.example.storyapps.ui.detail.DetailActivity
import com.example.storyapps.ui.detail.DetailActivity.Companion.EXTRA_DETAIL

class StoryAdapter(private val context: Context) :
    PagingDataAdapter<StoryEntity, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(val binding: ListItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        with(holder) {
            with(binding) {
                if (story != null) {
                    val circularProgressDrawable = CircularProgressDrawable(context)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()

                    Glide.with(itemView.context)
                        .load(story.photoUrl)
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.ic_broken_image)
                        .listener(glideListener(binding)).into(ivItemPhoto)
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
                            )
                        )
                        itemView.context.startActivity(
                            intent, optionsCompat.toBundle()
                        )
                    }
                }
            }
        }
        animateItem(holder.itemView, position)
    }

    private fun glideListener(
        binding: ListItemsBinding,
    ): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
            ): Boolean {
                binding.pbItem.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: com.bumptech.glide.load.DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.pbItem.visibility = View.GONE
                return false
            }

        }
    }

    private var lastPosition = -1
    private fun animateItem(view: View, position: Int) {
        val animatorSet = AnimatorSet()

        val alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.5f, 1f)
        if (position > lastPosition) {
            val translateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 1000f, 0f)

            animatorSet.playTogether(translateY, alpha)
        } else {
            val translateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -1000f, 0f)

            animatorSet.playTogether(translateY, alpha)
        }

        animatorSet.start()
        lastPosition = position
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}