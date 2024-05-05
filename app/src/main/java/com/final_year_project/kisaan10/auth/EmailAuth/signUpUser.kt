package com.final_year_project.kisaan10.auth.EmailAuth

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

// Function to sign up a new user
fun signUpUser(email: String, password: String, callback: (Boolean, Exception?) -> Unit) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null) // User signed up successfully
            } else {
                Log.e("SignUp", "Eamil : $email and password : $password")
                Log.e("SignUp", "Failed to sign up user", task.exception)
                callback(false, task.exception) // Failed to sign up user
            }
        })
}
