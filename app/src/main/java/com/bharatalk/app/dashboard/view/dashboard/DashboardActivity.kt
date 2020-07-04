package com.bharatalk.app.dashboard.view.dashboard

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.bharatalk.app.R
import com.bharatalk.app.dashboard.storage.backend.model.Comment
import com.bharatalk.app.dashboard.storage.backend.model.Comments
import com.bharatalk.app.dashboard.storage.backend.model.Talk
import com.bharatalk.app.dashboard.storage.backend.repository.FirestoreRepository
import com.bharatalk.app.dashboard.storage.backend.repository.FirestoreRepository.Companion.TOKS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity() {

    private lateinit var repository: FirestoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setup()
    }

    override fun setup() {
        initRepository()
        setClickListeners()
    }

    private fun initRepository() {
        repository = FirestoreRepository()
    }

    private fun setClickListeners() {
        uploadButton.setOnClickListener {
            Log.e("mytag", "debug 0")
            if(areFieldsValid()) {
                Log.e("mytag", "debug -1")
                pushTalkObject()
            }
            else  {
                Log.e("mytag", "debug -2")
                showInvalidFieldMessage()
            }
        }
    }

    private fun pushTalkObject() {
        Log.e("mytag", "pushing object")
        FirebaseFirestore.getInstance().collection(TOKS_COLLECTION).document().set(getTalk()).addOnSuccessListener {
            Log.e("mytag", "debug 9")
            emptyEditTexts()
        }.addOnFailureListener {
            Log.e("mytag", "debug 10")
            showInvalidFieldMessage()
        }
    }

    private fun emptyEditTexts() {
        Log.e("mytag", "debug 8")
        videoUrlEditText.text?.clear()
        userNameEditText.text?.clear()
        songNameEditText.text?.clear()
        shareCountEditText.text?.clear()
        likeCountEditText.text?.clear()
        hashTagEditText.text?.clear()
        commentCountEditText.text?.clear()

        Toast.makeText(
            applicationContext,
            "well done my boi",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun getTalk(): Talk {
        Log.e("mytag", "debug 7")

        val talk = Talk()
        talk.video_id = getText(videoUrlEditText)
        talk.user_name = getText(userNameEditText)
        talk.song_name = getText(songNameEditText)
        talk.share_count = getNumber(shareCountEditText)
        talk.line_count = getNumber(likeCountEditText)
        talk.hashtags = getHashTags(hashTagEditText)
        talk.comments = getComments()
        return  talk
    }

    private fun getComments(): Comments {
        Log.e("mytag", "debug 6")

        val comments = Comments()
        comments.comment_count = getNumber(commentCountEditText)
        comments.comment = getComment()
        return comments
    }

    private fun getComment(): List<Comment> {
        Log.e("mytag", "debug 5")

        val comment = ArrayList<Comment>()
        comment.add(Comment("good comment", "Mokshit jain"))
        comment.add(Comment("amazing comment", "Nikita jain"))
        return comment
    }

    private fun getHashTags(editText: EditText): List<String> {
        Log.e("mytag", "debug 4")

        val text = editText.text.toString()
        return text.split("\\s".toRegex())
    }

    private fun getText(editText: EditText): String {
        return editText.text.toString()
    }

    private fun getNumber(editText: EditText): Int {
        val text = editText.text.toString()
        val number = text.toInt()
        return number
    }

    private fun areFieldsValid(): Boolean {
        Log.e("mytag", "debug 3")
        if(
            isEmptyText(videoUrlEditText) || isEmptyText(userNameEditText) ||
            isEmptyText(songNameEditText) || isEmptyText(shareCountEditText) ||
            isEmptyText(likeCountEditText) || isEmptyText(hashTagEditText) ||
            isEmptyText(commentCountEditText)
        ) return false
        Log.e("mytag", "debug 2")
        return true
    }

    private fun isEmptyText(editText: EditText): Boolean {
        return editText.text.toString().isEmpty()
    }

    private fun showInvalidFieldMessage() {
        Log.e("mytag", "debug 1")
        Toast.makeText(
            applicationContext,
            "some field is wrong",
            Toast.LENGTH_SHORT
        ).show()
    }
}
