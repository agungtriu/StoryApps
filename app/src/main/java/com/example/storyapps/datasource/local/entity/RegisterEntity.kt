package com.example.storyapps.datasource.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterEntity(
    val error: Boolean? = null,
    val message: String? = null
) : Parcelable
