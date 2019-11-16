package com.slack.exercise.api

import com.google.gson.annotations.SerializedName

/**
 * User model returned by the API.
 */
data class User(val username: String,
                @SerializedName("display_name")
                val displayName: String,
                @SerializedName("avatar_url")
                val avatarUrl: String)