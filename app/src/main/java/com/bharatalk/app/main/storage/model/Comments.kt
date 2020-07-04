package com.bharatalk.app.main.storage.model

data class Comments(
    var comment_count: Int = 0,
    var comment: List<Comment> = ArrayList()
)