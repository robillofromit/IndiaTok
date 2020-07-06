package com.bharatalk.app.main.view.toks.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.bharatalk.app.Bharatalk.Companion.isFirstTokShown
import com.bharatalk.app.Bharatalk.Companion.isSecondTokShown
import com.bharatalk.app.R
import com.bharatalk.app.main.storage.model.Talk
import com.bharatalk.app.main.util.NumberUtil
import com.bharatalk.app.main.view.toks.adapter.ToksAdapter.Companion.activePosition
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.cell_tok.view.*
import java.lang.StringBuilder

class ToksHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var tokPosition: Int = -1
    private lateinit var currentVideoId: String
    private lateinit var youtubePlayer: YouTubePlayer

    private lateinit var tokListener: TokListener

    init {
        val parentView = itemView as ViewGroup

        var playerView: YouTubePlayerView? = null

        for(child in parentView.children) {
            if(child is YouTubePlayerView) {
                playerView = child
            }
        }

        playerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubePlayer = youTubePlayer
                youtubePlayer.cueVideo(currentVideoId, 0f)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)

                if(state == PlayerConstants.PlayerState.VIDEO_CUED) {
                    if(tokPosition == 0 && !isFirstTokShown && activePosition == tokPosition) {
                        isFirstTokShown = true
                        youTubePlayer.play()
                    }
                    else if(tokPosition == 1 && !isSecondTokShown && activePosition == tokPosition) {
                        isSecondTokShown = true
                        youTubePlayer.play()
                    }
                }

                if(state == PlayerConstants.PlayerState.ENDED) {
                    if(::tokListener.isInitialized)
                        tokListener.onTokEnded(tokPosition)
                }
                if(state != PlayerConstants.PlayerState.PLAYING)
                    itemView.soundTrackNameTv.isSelected = false
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun cueVideo(talk: Talk, tokPosition: Int) {
        itemView.userNameTv.text = "@${talk.user_name}"
        itemView.soundTrackNameTv.text = talk.song_name

        itemView.userTagsTv.text = getTagsString(talk.hashtags)

        itemView.shareTv.text =
        String.format(itemView.context.getString(R.string.likes), NumberUtil.format(talk.share_count))
        itemView.likeTv.text =
            String.format(itemView.context.getString(R.string.likes), NumberUtil.format(talk.line_count))
        itemView.commentTv.text =
            String.format(itemView.context.getString(R.string.comments), NumberUtil.format(talk.comments.comment_count))

        itemView.soundTrackNameTv.isSelected = true
        this.tokPosition = tokPosition
        currentVideoId = talk.video_id
        if(!::youtubePlayer.isInitialized) return
        youtubePlayer.cueVideo(talk.video_id, 0f)

        itemView.likeHolder.setOnClickListener {
            if(::tokListener.isInitialized) {
                tokListener.onLikeShareCommentClicked()
            }
        }

        itemView.shareHolder.setOnClickListener {
            if(::tokListener.isInitialized) {
                tokListener.onLikeShareCommentClicked()
            }
        }

        itemView.commentHolder.setOnClickListener {
            if(::tokListener.isInitialized) {
                tokListener.onLikeShareCommentClicked()
            }
        }
    }

    private fun getTagsString(hashTags: List<String>): String {
        val builder = StringBuilder()
        for(tag in hashTags) {
            builder.append("#${tag} ")
        }
        return builder.toString()
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
        fun onLikeShareCommentClicked()
    }
}