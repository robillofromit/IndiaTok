package com.bharatalk.app.main.view.toks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.bharatalk.app.R
import com.bharatalk.app.main.storage.model.Talk
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ToksAdapter(
    private var tokList: List<Talk>, private val lifecycle: Lifecycle
): RecyclerView.Adapter<ToksHolder>(), ToksHolder.TokListener {

    private var rateAppSheetShowCounter = 10

    companion object{
        var activePosition: Int = 0
    }

    private lateinit var tokListener: TokListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToksHolder {
        val completeView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_tok, parent, false)

        val parentView = completeView as ViewGroup
        var playerView: YouTubePlayerView? = null

        for(child in parentView.children) {
            if(child is YouTubePlayerView) {
                playerView = child
            }
        }

        playerView?.let {
            lifecycle.addObserver(it)
        }

        return ToksHolder(completeView)
    }

    override fun getItemCount(): Int {
        return tokList.size
    }

    override fun onBindViewHolder(holder: ToksHolder, position: Int) {
        holder.cueVideo(tokList[position], position)
        holder.setTokListener(this)
    }

    override fun onTokEnded(tokPosition: Int) {
        if(::tokListener.isInitialized)
            tokListener.onTokEnded(tokPosition)
    }

    override fun onLikeShareCommentClicked() {
        if(::tokListener.isInitialized)
            tokListener.onLikeShareCommentClicked()
    }

    fun setTokListener(tokListener: TokListener) {
        this.tokListener = tokListener
    }

    fun setToks(toksList: List<Talk>) {
        this.tokList = toksList
        notifyDataSetChanged()
    }

    fun setActivePosition(lastVisiblePosition: Int) {
        rateAppSheetShowCounter -= 1
        if(rateAppSheetShowCounter == 0) {
            if(::tokListener.isInitialized)
                tokListener.showComingSoonSheet()
        }

        activePosition = lastVisiblePosition
    }

    interface TokListener {
        fun onTokEnded(tokPosition: Int)
        fun onLikeShareCommentClicked()
        fun showComingSoonSheet()
    }
}