package com.bharatalk.app.main.view.toks

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bharatalk.app.R
import com.bharatalk.app.main.storage.model.Talk
import com.bharatalk.app.main.storage.repository.FirestoreRepository
import com.bharatalk.app.main.util.PreferenceUtil
import com.bharatalk.app.main.util.PreferenceUtil.Companion.DAY_OF_YEAR
import com.bharatalk.app.main.util.PreferenceUtil.Companion.SHOULD_SHOW_COMING_SOON_SHEET_TODAY
import com.bharatalk.app.main.view.base.BaseActivity
import com.bharatalk.app.main.view.coming_soon.ComingSoonBottomSheet
import com.bharatalk.app.main.view.swipe_up.SwipeUpFragment
import com.bharatalk.app.main.view.toks.adapter.ToksAdapter
import com.bharatalk.app.main.view.toks.adapter.ToksHolder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_toks.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ToksActivity : BaseActivity(), ToksAdapter.TokListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var toksList: MutableList<Talk>
    private lateinit var toksAdapter: ToksAdapter
    private var lastVisiblePosition: Int = -1
    private var swipeUpMessageCount = 3
    private lateinit var swipeUpFragment: SwipeUpFragment
    private val swipeUpFragmentTag = "swipe_up"
    private lateinit var comingSoonSheet: ComingSoonBottomSheet
    private lateinit var preferences: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toks)

        setup()
    }

    override fun setup() {
        setStatusBarColor(R.color.black)

        toksList = ArrayList()
        toksAdapter = ToksAdapter(toksList, lifecycle)
        toksAdapter.setTokListener(this)
        recyclerView.adapter = toksAdapter
        PagerSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.setHasFixedSize(true)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visiblePosition: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (visiblePosition > -1) {
                    swipeUpMessageCount -= 1
                    hideSwipeUpFragment()
                    toksAdapter.setActivePosition(visiblePosition)
                    lastVisiblePosition = visiblePosition
                    layoutManager.findViewByPosition(visiblePosition)?.let {
                        val holder = recyclerView.findViewHolderForAdapterPosition(visiblePosition) as ToksHolder
                        holder.playVideo()
                    }
                }
            }
        })

        getToksList()

        setSwipeRefreshListener()

        if(swipeUpMessageCount > 0) {
            if(!::swipeUpFragment.isInitialized)
                swipeUpFragment = SwipeUpFragment.newInstance()

            showSwipeUpFragment()
        }

        initPreferences()

        setClickListeners()
    }

    private fun setClickListeners() {
        addTokButton.setOnClickListener {
            onLikeShareCommentClicked()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initPreferences() {
        preferences = PreferenceUtil(PreferenceManager(this).sharedPreferences)

        setShouldShowComingSoonSheetAutomatically()
    }

    private fun setShouldShowComingSoonSheetAutomatically() {
        val dayOfYearPreferences = preferences.getInt(DAY_OF_YEAR)
        val dayOfYearCalendar = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        if(dayOfYearCalendar != dayOfYearPreferences && !preferences.getBoolean(SHOULD_SHOW_COMING_SOON_SHEET_TODAY, true)) {
            preferences.putInt(DAY_OF_YEAR, dayOfYearCalendar)
            preferences.putBoolean(SHOULD_SHOW_COMING_SOON_SHEET_TODAY, true)
        }
    }

    private fun showSwipeUpFragment() {
        if(swipeUpFragment.isAdded || swipeUpFragment.isVisible) return

        supportFragmentManager
            .beginTransaction()
            .add(R.id.parentView, swipeUpFragment, swipeUpFragmentTag)
            .commit()
    }

    private fun hideSwipeUpFragment() {
        if(swipeUpMessageCount > 0) return

        try {
            supportFragmentManager.findFragmentByTag(swipeUpFragmentTag)?.let {
                supportFragmentManager.beginTransaction().remove(it).commit()
            }
        }
        catch (ignored: Exception) {}
    }

    private fun setSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun getToksList(): List<Talk> {
        FirestoreRepository().getToks().addOnSuccessListener {
            swipeRefreshLayout.isRefreshing = false
            it?.let {
                if(it.isEmpty) {
                    showErrorRetrySnackBar()
                }
                else {
                    toksList = it.toObjects(Talk::class.java)
                    toksList.shuffle()
                    renderForToksReceived(toksList)
                }
            } ?: run {
                showErrorRetrySnackBar()
            }
        }.addOnFailureListener {
            swipeRefreshLayout.isRefreshing = false
            showErrorRetrySnackBar()
        }
        return ArrayList()
    }

    private fun renderForToksReceived(toksList: List<Talk>) {
        toksAdapter.setToks(toksList)
    }

    override fun onTokEnded(tokPosition: Int) {
        if(toksList.size > tokPosition + 2) {
            recyclerView.smoothScrollToPosition(tokPosition + 1)
        }
    }

    private fun showErrorRetrySnackBar() {
        errorHolder.visibility = View.VISIBLE

        val snackBar = Snackbar.make(
            parentView, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.retry)) {
            errorHolder.visibility = View.GONE
            getToksList()
        }
        snackBar.show()
    }

    override fun onRefresh() {
        getToksList()
    }

    override fun onLikeShareCommentClicked() {

        if(!::comingSoonSheet.isInitialized)
            comingSoonSheet = ComingSoonBottomSheet.newInstance()

        if(!comingSoonSheet.isAdded && !comingSoonSheet.isVisible)
            comingSoonSheet.show(supportFragmentManager, comingSoonSheet.tag)
    }

    override fun showComingSoonSheet() {
        if(preferences.getBoolean(SHOULD_SHOW_COMING_SOON_SHEET_TODAY)) {
            preferences.putBoolean(SHOULD_SHOW_COMING_SOON_SHEET_TODAY, false)
            onLikeShareCommentClicked()
        }
    }
}