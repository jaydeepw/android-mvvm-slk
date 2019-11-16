package com.slack.exercise.ui.usersearch

import android.os.Bundle
import com.slack.exercise.R
import dagger.android.support.DaggerAppCompatActivity

/**
 * Launcher activity. Kept light and simple to delegate view logic to fragment(s) it attaches.
 */
class UserSearchActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)
    }
}
