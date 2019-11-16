package com.slack.exercise.dagger

import com.slack.exercise.api.SlackApi
import com.slack.exercise.api.SlackApiImpl
import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.dataprovider.UserSearchResultDataProviderImpl
import dagger.Binds
import dagger.Module

/**
 * Module to setup Application scoped instances that require providers.
 */
@Module
abstract class AppModule {
    @Binds
    abstract fun provideUserSearchResultDataProvider(dataProvider: UserSearchResultDataProviderImpl): UserSearchResultDataProvider

    @Binds
    abstract fun provideSlackApi(apiImpl: SlackApiImpl): SlackApi
}