package com.final_year_project.kisaan10.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.final_year_project.kisaan10.FirebaseManager
import com.final_year_project.kisaan10.components.showToast
import com.final_year_project.kisaan10.model.UserInfo
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<UserInfo>()
//    val user: LiveData<UserInfo> = _user

    private val firestoreDB = FirebaseManager.getFireStore()

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            try {
                val userDoc = firestoreDB.collection("users").document(userId).get().await()
                val userData = userDoc.toObject<UserInfo>()
                _user.value = userData
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun saveUserToFireStore(userId: String, userInfo: UserInfo) {
        val firestoreDB = FirebaseManager.getFireStore()

        val userDocRef = firestoreDB.collection("users").document(userId)
        userDocRef.set(userInfo)
            .addOnSuccessListener {
                Log.v("Dataa","Data saved successfully")
            }
            .addOnFailureListener { e ->
                // Handle error
                Log.v("Dataa","Data don't saved ${e.message}")
            }
    }
}