package com.slack.exercise.ui.usersearch

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.*
import android.view.*
import com.slack.exercise.BlackListCopyService
import com.slack.exercise.R
import com.slack.exercise.model.UserSearchResult
import dagger.android.support.DaggerFragment
import kotterknife.bindView
import timber.log.Timber
import java.io.*
import javax.inject.Inject


/**
 * Main fragment displaying and handling interactions with the view.
 * We use the MVP pattern and attach a Presenter that will be in charge of non view related operations.
 */
class UserSearchFragment : DaggerFragment(), UserSearchContract.View {

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val userSearchResultList: RecyclerView by bindView(R.id.user_search_result_list)
    private val blacklistSet = mutableSetOf<String>()

    @Inject
    internal lateinit var presenter: UserSearchPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_user_search, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpList()

        CacheTask {
            cacheBlackListDataInMemory()
        }.execute("")

        // copy standard blacklist data to internal storage.
        // There is a catch here. Everytime the app updates with a new blacklist
        // information, we should device an algorithm to copy all the data over again
        // and unify the old and new blacklist data on the internal storage.
        startBlackListCopyService()
    }

    /**
     * Asynchronously, cache the data in the memory
     */
    open class CacheTask(val cacheFunction: () -> Unit) : AsyncTask<String, Void, Unit>() {
        override fun doInBackground(vararg params: String?) {
            cacheFunction()
        }
    }

    /**
     * Keep all the blacklist information in memory for faster access and also so that
     * we dont have to query the files on the internal storage.
     */
    private fun cacheBlackListDataInMemory() {
        val file = File(activity?.filesDir, BlackListCopyService.BLACKLIST_FILE_NAM)

        try {
            val inputStream = FileInputStream(file);
            val inputreader = InputStreamReader(inputStream)
            val buffreader = BufferedReader(inputreader)
            var line: String?
            while (buffreader.readLine().also { line = it } != null) {
                if (line != null) {
                    blacklistSet.add(line!!)
                }
            }
        } catch (e: IOException) {
            Timber.e("${e.message}")
        } catch (e: FileNotFoundException) {
            Timber.e("${e.message}")
        } catch (e: Exception) {
            Timber.e("${e.message}")
        }

        Timber.d("blacklistSet.size: ${blacklistSet.size}")
    }

    private fun startBlackListCopyService() {
        val intent = Intent(activity, BlackListCopyService::class.java)
        activity?.startService(intent)
    }

    override fun onStart() {
        super.onStart()

        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()

        presenter.detach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_search, menu)

        val searchView: SearchView = menu.findItem(R.id.search_menu_item).actionView as SearchView
        searchView.queryHint = getString(R.string.search_users_hint)
        // following line will make the searchview spead wide completely along the Toolbar
        // https://stackoverflow.com/a/34050959/452487
        searchView.maxWidth = Integer.MAX_VALUE;
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (!isBlacklisted(newText)) {
                    presenter.onQueryTextChange(newText)
                }
                return true
            }
        })
    }

    fun isBlacklisted(query: String) : Boolean {
        return blacklistSet.contains(query)
    }

    override fun onUserSearchResults(results: Set<UserSearchResult>) {
        val adapter = userSearchResultList.adapter as UserSearchAdapter
        if (results.isEmpty()) {
            // todo: Save blacklisted data
        } else {
            adapter.setResults(results)
        }
    }

    override fun onUserSearchError(error: Throwable) {
        Timber.e(error, "Error searching users.")
    }

    private fun setUpToolbar() {
        val act = activity as UserSearchActivity
        act.setSupportActionBar(toolbar)
    }

    private fun setUpList() {
        with(userSearchResultList) {
            val divider = DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
            divider.setDrawable(
                ContextCompat.getDrawable(context, R.drawable.item_divider)!!
            )
            addItemDecoration(divider)
            adapter = UserSearchAdapter()
            layoutManager = LinearLayoutManager(activity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
        }
    }
}