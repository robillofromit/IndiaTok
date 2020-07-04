package com.bharatalk.app.main.view.toks

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bharatalk.app.R
import com.bharatalk.app.main.storage.model.Talk
import com.bharatalk.app.main.storage.repository.FirestoreRepository
import com.bharatalk.app.main.view.base.BaseActivity
import com.bharatalk.app.main.view.coming_soon.ComingSoonBottomSheet
import com.bharatalk.app.main.view.toks.adapter.ToksAdapter
import com.bharatalk.app.main.view.toks.adapter.ToksHolder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_toks.*

class ToksActivity : BaseActivity(), ToksAdapter.TokListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var comingSoonSheet: ComingSoonBottomSheet
    private lateinit var toksList: List<Talk>
    private lateinit var toksAdapter: ToksAdapter

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
                    layoutManager.findViewByPosition(visiblePosition)?.let {
                        val holder = recyclerView.findViewHolderForAdapterPosition(visiblePosition) as ToksHolder
                        holder.playVideo()
                    }
                }
            }
        })

        getToksList()

        setSwipeRefreshListener()
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
        Log.e("mytag", "tok end $tokPosition")
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
}