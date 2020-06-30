package com.example.indiatok.toks

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.indiatok.R
import com.example.indiatok.base.BaseActivity
import com.example.indiatok.toks.adapter.ToksAdapter
import com.example.indiatok.toks.adapter.ToksHolder
import kotlinx.android.synthetic.main.activity_toks.*

class ToksActivity : BaseActivity(), ToksAdapter.TokListener {

    private lateinit var toksList: List<String>

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

        toksList = getToksList()
        val toksAdapter = ToksAdapter(toksList, lifecycle)
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
                        Log.e("mytag", "position is $visiblePosition")
                        val holder = recyclerView.findViewHolderForAdapterPosition(visiblePosition) as ToksHolder
                        holder.playVideo()
                    }
                }
            }
        })
    }

    private fun getToksList(): List<String> {
        val toksList: MutableList<String> = ArrayList()
        toksList.add("KRntP-q_R9s")
        toksList.add("bdPZ2Cu1vNU")
        toksList.add("nVzA1uWTydQ")
        toksList.add("pz95u3UVpaM")
        toksList.add("ZYP1UJylTAU")
        toksList.add("Ah0Ys50CqO8")
        toksList.add("HSCymCubvhk")
        toksList.add("nyRawESIuy4")
        return toksList
    }

    override fun onTokEnded(tokPosition: Int) {
        if(toksList.size > tokPosition + 2) {
            recyclerView.smoothScrollToPosition(tokPosition + 1)
        }
    }
}
