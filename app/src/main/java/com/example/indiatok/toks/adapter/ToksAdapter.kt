package com.example.indiatok.toks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.indiatok.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ToksAdapter(
    private val tokList: List<String>, private val lifecycle: Lifecycle
): RecyclerView.Adapter<ToksHolder>(), ToksHolder.TokListener {

    private lateinit var tokListener: TokListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToksHolder {
        val completeView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_tok, parent, false)

        val playerView = completeView as YouTubePlayerView
        lifecycle.addObserver(playerView)
        return ToksHolder(playerView)
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

    fun setTokListener(tokListener: TokListener) {
        this.tokListener = tokListener
    }

    interface TokListener {
        fun onTokEnded(tokPosition: Int)
    }
}