package com.slack.exercise.ui.dagger

import com.slack.exercise.ui.usersearch.UserSearchActivity
import com.slack.exercise.ui.usersearch.UserSearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Module to declare UI components that have injectable members
 */
@Module
abstract class BindingModule {
    @ContributesAndroidInjector
    abstract fun bindUserSearchActivity(): UserSearchActivity

    @ContributesAndroidInjector
    abstract fun bindUserSearchFragment(): UserSearchFragment
}