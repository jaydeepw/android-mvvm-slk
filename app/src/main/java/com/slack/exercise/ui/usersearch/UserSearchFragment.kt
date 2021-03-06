package com.slack.exercise.ui.usersearch

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.*
import android.view.*
import android.widget.TextView
import com.slack.exercise.R
import com.slack.exercise.model.UserSearchResult
import com.slack.exercise.services.BlackListCopyService
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

    private var searchView: SearchView? = null
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val emptyText: TextView by bindView(R.id.message_title)
    private val userSearchResultList: RecyclerView by bindView(R.id.user_search_result_list)
    private val blacklistSet = mutableSetOf<String>()

    @Inject
    internal lateinit var presenter: UserSearchPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        searchView = menu.findItem(R.id.search_menu_item).actionView as SearchView
        searchView?.queryHint = getString(R.string.search_users_hint)
        // following line will make the searchview spead wide completely along the Toolbar
        // https://stackoverflow.com/a/34050959/452487
        searchView?.maxWidth = Integer.MAX_VALUE;
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var queryVar = newText.trim()
                if (!isBlacklisted(queryVar)) {
                    presenter.onQueryTextChange(queryVar)
                } else {
                    Timber.w("Not making an API call")
                    showNoResults()
                }
                return true
            }
        })
    }

    fun isBlacklisted(query: String): Boolean {
        return blacklistSet.contains(query)
    }

    override fun onUserSearchResults(results: Set<UserSearchResult>) {
        val query = searchView?.query?.toString() ?: ""
        if (results.isEmpty()) {
            // save queries that dont yield any result in the file

            if (!blacklistSet.contains(query.trim())) {
                // dont save the query if it was already cached.
                // this means while caching first time itself, it
                // was saved in the storage.
                updateBlackList(query)
            }
            showNoResults()
        } else {
            showResults(results)
        }
    }

    private fun showResults(
        results: Set<UserSearchResult>
    ) {
        // hide previous messages
        userSearchResultList.visibility = View.VISIBLE
        emptyText.visibility = View.GONE

        val adapter = userSearchResultList.adapter as UserSearchAdapter
        adapter.setResults(results)
    }

    private fun showNoResults() {
        userSearchResultList.visibility = View.GONE
        emptyText.visibility = View.VISIBLE
        emptyText.text = getString(R.string.msg_no_results_search)
    }

    private fun updateBlackList(query: String) {
        var queryVar = query
        queryVar = query.trim()

        if (queryVar.isBlank()) {
            return
        }

        blacklistSet.add(queryVar)
        CacheTask {
            val file = File(activity?.filesDir, BlackListCopyService.BLACKLIST_FILE_NAM)
            val inputStream = FileInputStream(file)
            var allText = inputStream.bufferedReader().use(BufferedReader::readText)
            allText += queryVar
            Timber.d("$allText")
            BlackListCopyService.updateBlackLisData(
                activity?.filesDir!!,
                allText
            )
        }.execute("")
    }

    override fun onUserSearchError(error: Throwable) {
        Timber.e(error, "Error searching users.")
        userSearchResultList.visibility = View.GONE
        emptyText.visibility = View.VISIBLE
        emptyText.text = error.message
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