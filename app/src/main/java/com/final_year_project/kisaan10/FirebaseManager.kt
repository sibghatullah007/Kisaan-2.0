package com.final_year_project.kisaan10

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseManager {
    private var firestore: FirebaseFirestore? = null

    // Initialize Firebase FireStore
    fun init(context: Context) {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance()
        }
    }

    // Get Firebase FireStore instance
    fun getFireStore(): FirebaseFirestore {
        if (firestore == null) {
            throw IllegalStateException("Firebase Firestore is not initialized.")
        }
        return firestore!!
    }
}
