package com.slack.exercise.model

/**
 * Models users returned by the API.
 */
data class UserSearchResult(
    val username: String,
    val id: Long,
    val avatar_url: String,
    val display_name: String
)