package com.slack.exercise.ui.usersearch

import com.slack.exercise.dataprovider.UserSearchResultDataProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Presenter responsible for reacting to user inputs and initiating search queries.
 */
class UserSearchPresenter @Inject constructor(
        private val userNameResultDataProvider: UserSearchResultDataProvider
) : UserSearchContract.Presenter {

    private var view: UserSearchContract.View? = null
    private val searchQuerySubject = PublishSubject.create<String>()
    private var searchQueryDisposable = Disposables.disposed()

    override fun attach(view: UserSearchContract.View) {
        this.view = view

        searchQueryDisposable = searchQuerySubject
                .flatMapSingle { searchTerm ->
                    if (searchTerm.isEmpty()) {
                        Single.just(emptySet())
                    } else {
                        userNameResultDataProvider.fetchUsers(searchTerm)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { results -> this@UserSearchPresenter.view?.onUserSearchResults(results) },
                        { error -> this@UserSearchPresenter.view?.onUserSearchError(error) }
                )
    }

    override fun detach() {
        view = null
        searchQueryDisposable.dispose()
    }

    override fun onQueryTextChange(searchTerm: String) {
        searchQuerySubject.onNext(searchTerm)
    }
}