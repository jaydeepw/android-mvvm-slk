package com.slack.exercise.ui.usersearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return UserSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userSearchResults.size
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        showAvatar(holder, position)
        showName(holder, position)
        showUsername(holder, position)
    }

    private fun showName(holder: UserSearchViewHolder, position: Int) {
        holder.displayName.text = userSearchResults[position].display_name
    }

    private fun showAvatar(holder: UserSearchViewHolder, position: Int) {
        val url: String = userSearchResults[position].avatar_url ?: ""
        if (url.isBlank()) {
            holder.avatar.setImageDrawable(null)
        } else {
            Glide.with(holder.itemView.context).load(url).into(holder.avatar)
        }
    }

    private fun showUsername(
        holder: UserSearchViewHolder,
        position: Int
    ) {
        holder.username.text = userSearchResults[position].username
    }

    class UserSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val displayName: TextView = itemView.display_name
        val username: TextView = itemView.username
        val avatar: ImageView = itemView.avatar
    }
}