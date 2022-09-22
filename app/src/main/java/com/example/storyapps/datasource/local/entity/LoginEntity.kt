package com.example.storyapps.datasource.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginEntity(
    val name: String? = null,
    val userId: String? = null,
    val token: String? = null
) : Parcelable
