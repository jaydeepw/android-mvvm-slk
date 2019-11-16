package com.slack.exercise.ui.usersearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.slack.exercise.R
import com.slack.exercise.model.UserSearchResult
import kotlinx.android.synthetic.main.item_user_search.view.*

/**
 * Adapter for the list of [UserSearchResult].
 */
class UserSearchAdapter : RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {
    private var userSearchResults: List<UserSearchResult> = emptyList()

    fun setResults(results: Set<UserSearchResult>) {
        userSearchResults = results.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return UserSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userSearchResults.size
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        holder.username.text = userSearchResults[position].username
    }

    class UserSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.username
    }
}