package com.slack.exercise.api

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

private const val API_URL = "https://slack-users.herokuapp.com/"

/**
 * Implementation of [SlackApi] using [UserSearchService] to perform the API requests.
 */
@Singleton
class SlackApiImpl @Inject constructor() : SlackApi {
    private val service: UserSearchService

    init {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()

        service = Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(UserSearchService::class.java)
    }

    override fun searchUsers(searchTerm: String): Single<List<User>> {
        return service.searchUsers(searchTerm)
                .map {
                    it.users
                }
                .subscribeOn(Schedulers.io())

    }
}