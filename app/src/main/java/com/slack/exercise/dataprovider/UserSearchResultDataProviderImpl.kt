package com.slack.exercise.dataprovider

import com.slack.exercise.api.SlackApi
import com.slack.exercise.model.UserSearchResult
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [UserSearchResultDataProvider].
 */
@Singleton
class UserSearchResultDataProviderImpl @Inject constructor(private val slackApi: SlackApi) : UserSearchResultDataProvider {

    /**
     * Returns a [Single] emitting a set of [UserSearchResult].
     */
    override fun fetchUsers(searchTerm: String): Single<Set<UserSearchResult>> {
        return slackApi.searchUsers(searchTerm)
                .map {
                    it.map { user -> UserSearchResult(user.username) }.toSet()
                }
    }
}