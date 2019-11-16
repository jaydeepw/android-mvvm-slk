package com.slack.exercise.ui.usersearch

import com.slack.exercise.model.UserSearchResult

/**
 * MVP contract for User Search.
 */
interface UserSearchContract {

    /**
     * Callbacks to notify the view of the outcome of search queries initiated.
     */
    interface View {
        /**
         * Call when [UserSearchResult] are returned.
         */
        fun onUserSearchResults(results: Set<UserSearchResult>)

        /**
         * Call when an error occurs during the execution of search queries.
         */
        fun onUserSearchError(error: Throwable)
    }

    interface Presenter {
        /**
         * Call to attach a [Presenter] and provide its [View].
         */
        fun attach(view: View)

        /**
         * Call to detach a [Presenter] and clean up resources.
         */
        fun detach()

        /**
         * Notifies the presenter that the [searchTerm] has changed.
         */
        fun onQueryTextChange(searchTerm: String)
    }
}