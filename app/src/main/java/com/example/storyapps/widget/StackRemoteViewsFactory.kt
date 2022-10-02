package com.example.storyapps.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.storyapps.R
import com.example.storyapps.datasource.local.StoryDatabase
import com.example.storyapps.datasource.local.entity.StoryFavoriteEntity
import com.example.storyapps.utils.Config.Companion.DATABASE_NAME


internal class StackRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()
    private lateinit var storyEntities: List<StoryFavoriteEntity>

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        mWidgetItems.clear()
        val db = Room.databaseBuilder(context, StoryDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries().build()
        storyEntities = db.storyDao().loadStoriesWidget()
        for (stories in storyEntities) {
            try {
                mWidgetItems.add(
                    Glide.with(context.applicationContext).asBitmap().load(stories.photoUrl)
                        .submit(SIZE_ORIGINAL, SIZE_ORIGINAL).get()
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        db.close()
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    @SuppressLint("RemoteViewLayout")
    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.iv_widget_item, mWidgetItems[position])

        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.iv_widget_item, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}