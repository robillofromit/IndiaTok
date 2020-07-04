package com.bharatalk.app.main.view.toks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bharatalk.app.R
import com.bharatalk.app.dashboard.storage.backend.model.TalksList
import com.bharatalk.app.main.storage.model.Talk
import com.bharatalk.app.main.storage.repository.FirestoreRepository
import com.bharatalk.app.main.view.base.BaseActivity
import com.bharatalk.app.main.view.toks.adapter.ToksAdapter
import com.bharatalk.app.main.view.toks.adapter.ToksHolder
import kotlinx.android.synthetic.main.activity_toks.*

class ToksActivity : BaseActivity(), ToksAdapter.TokListener {

    private lateinit var toksList: List<Talk>
    private lateinit var toksAdapter: ToksAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ToksActivity::class.java)
        }
    }

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
    }

    private fun getToksList(): List<Talk> {
        FirestoreRepository().getToks().addOnSuccessListener {
            Log.e("mytag","toks list fetched ${it.size()}")
            it?.let {
                val toksList = it.toObjects(Talk::class.java)
                renderForToksReceived(toksList)
                Log.e("mytag","talks list is ${toksList.size} ${toksList[0].video_id}")
            } ?: run {
                Log.e("mytag","no documents in successful result")
            }
        }.addOnFailureListener {
            Log.e("mytag","toks fetch failed ${it.printStackTrace()}")
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
}
