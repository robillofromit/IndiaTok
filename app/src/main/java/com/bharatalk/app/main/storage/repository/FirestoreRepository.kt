package com.bharatalk.app.main.storage.repository

import com.bharatalk.app.main.storage.model.Talk
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository {

    companion object {
        const val TOKS_COLLECTION = "toks"
    }

    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getDatabase(): FirebaseFirestore {
        return database
    }

    fun getToks(): Task<QuerySnapshot> {
        return database.collection(TOKS_COLLECTION).get()
    }
}