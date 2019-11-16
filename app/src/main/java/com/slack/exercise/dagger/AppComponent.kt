package com.slack.exercise.dagger

import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.ui.dagger.BindingModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Component providing Application scoped instances.
 */
@Singleton
@Component(modules = [AppModule::class, AndroidSupportInjectionModule::class, BindingModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun userSearchResultDataProvider(): UserSearchResultDataProvider
}