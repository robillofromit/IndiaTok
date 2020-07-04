package com.bharatalk.app.dashboard.storage.backend.repository

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    companion object {
        const val TOKS_COLLECTION = "toks"
    }

    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun x() {

    }

    fun getDatabase(): FirebaseFirestore {
        return database
    }
}