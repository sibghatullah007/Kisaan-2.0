package com.final_year_project.kisaan10.auth.EmailAuth

import android.util.Log
import com.final_year_project.kisaan10.FirebaseManager
import com.final_year_project.kisaan10.ViewModel.UserViewModel
import com.final_year_project.kisaan10.model.UserInfo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

// Function to sign up a new user
fun signUpUser(username:String, email: String, password: String, callback: (Boolean, Exception?) -> Unit) {
    val userInfo = UserInfo(username,email,Timestamp.now())
    val userViewModel = UserViewModel()
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid
                Log.v("Dataa",userInfo.toString())
                if (userId != null) {
                    userViewModel.saveUserToFireStore(userId,userInfo)
                }
                callback(true, null) // User signed up successfully
            } else {
                Log.e("SignUp", "Eamil : $email and password : $password")
                Log.e("SignUp", "Failed to sign up user", task.exception)
                callback(false, task.exception) // Failed to sign up user
            }
        })
}
