package com.dicoding.githubsearch

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var User: String? = null,
    var Photo: String? = null,
    var Following: String? = null,
    var Followers: String? = null
): Parcelable
