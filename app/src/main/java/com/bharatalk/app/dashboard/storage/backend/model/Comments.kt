package com.bharatalk.app.dashboard.storage.backend.model

data class Comments(
    var comment_count: Int = 0,
    var comment: List<Comment> = ArrayList()
)