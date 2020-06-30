package com.example.indiatok.toks.adapter

import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class ToksHolder(playerView: YouTubePlayerView): RecyclerView.ViewHolder(playerView) {

    private var tokPosition: Int = 0
    private lateinit var currentVideoId: String
    private lateinit var youtubePlayer: YouTubePlayer

    private lateinit var tokListener: TokListener

    init {
        playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer
                youtubePlayer.cueVideo(currentVideoId, 0f)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                if(state == PlayerConstants.PlayerState.ENDED) {
                    if(::tokListener.isInitialized)
                        tokListener.onTokEnded(tokPosition)
                }
            }
        })
    }

    fun cueVideo(videoId: String, tokPosition: Int) {
        this.tokPosition = tokPosition
        currentVideoId = videoId
        if(!::youtubePlayer.isInitialized) return
        youtubePlayer.cueVideo(videoId, 0f)
    }

    fun playVideo() {
        if(!::youtubePlayer.isInitialized) return
        youtubePlayer.play()
    }

    fun setTokListener(tokListener: TokListener) {
        this.tokListener = tokListener
    }

    interface TokListener {
        fun onTokEnded(tokPosition: Int)
    }
}