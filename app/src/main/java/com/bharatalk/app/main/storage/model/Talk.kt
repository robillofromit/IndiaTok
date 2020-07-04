package com.bharatalk.app.main.storage.model

data class Talk(
    var video_id: String = "",
    var user_name: String = "",
    var song_name: String = "",
    var share_count: Int = 0,
    var line_count: Int = 0,
    var hashtags: List<String> = ArrayList(),
    var comments: Comments = Comments()
)