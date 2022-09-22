package com.example.storyapps.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.storyapps.datasource.local.entity.StoryEntity

class StoryDiffCallback(
    private val oldList: List<StoryEntity>, private val newList: List<StoryEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldAccount = oldList[oldItemPosition]
        val newAccount = newList[newItemPosition]
        return oldAccount.name == newAccount.name && oldAccount.photoUrl == newAccount.photoUrl && oldAccount.description == newAccount.description
    }

}