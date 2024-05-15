package com.final_year_project.kisaan10.auth.googleAuth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.ViewModel.UserViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private  val context:Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth



    suspend fun signInWithEmailPassword(email: String, password: String): SignInResult {
        return try {
            // Create user with email and password
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            val user = userCredential.user

            // Retrieve username from Firestore based on email
            val username = getUserUsernameFromFirestore(email)

            // Create UserData object with retrieved username
            SignInResult(
                data = user?.let { userData ->
                    UserData(
                        userId = userData.uid,
                        username = username,
                        userEmail = userData.email,
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(
                data = null,
                errorMessage = e.message  )
        }

    }
    suspend fun getUserUsernameFromFirestore(email: String): String? {
        // Assuming you have a collection named "users" in Firestore
        val usersCollection = FirebaseFirestore.getInstance().collection("users")
        val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
        if (!querySnapshot.isEmpty) {
            val unamee = querySnapshot.documents[0].getString("name")
            Log.v("assHole", unamee.toString())
        } else {
            Log.v("assHole", "False")
        }
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].getString("name")
        } else {
            null
        }
    }
    suspend fun signUpUser(username: String, email: String, password: String, callback: (Boolean, Exception?) -> Unit) {
        val userInfo = com.final_year_project.kisaan10.model.UserInfo(username, email, Timestamp.now())
        val userViewModel = UserViewModel()

        // Create user with email and password
        GoogleAuthUiClient(context, oneTapClient).signInWithEmailPassword(email, password)
            .let { signInResult ->
                if (signInResult.data != null) {
                    val userId = signInResult.data.userId
                    Log.v("Dataa", userInfo.toString())
                    if (userId != null) {
                        // Save user info to Firestore
                        userViewModel.saveUserToFireStore(userId, userInfo)
                        callback(true, null) // User signed up successfully
                    } else {
                        Log.e("SignUp", "Failed to get user ID after sign up")
                        callback(false, Exception("Failed to get user ID after sign up"))
                    }
                } else {
                    Log.e("SignUp", "Failed to sign up user")
                    callback(false, Exception(signInResult.errorMessage ?: "Failed to sign up user"))
                }
            }
    }







    suspend fun signIn(): IntentSender?{
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            )
                .await()
        }
        catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }
    suspend fun signInWithIntent(intent: Intent):SignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredential).await().user
            SignInResult(
                data = user?.run{
                    UserData(
                        userId = uid,
                        username = displayName,
                        userEmail = email,
                    )
                },
                errorMessage = null
            )
        }
        catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }
        catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    suspend fun getSignedInUser(): UserData? = auth.currentUser?.run {
        val username = if (displayName == null || displayName.toString()=="") {
            Log.v("dddddd",displayName.toString())
            getUserUsernameFromFirestore(email!!)
        } else {
            displayName

        }
        UserData(
            userId = uid,
            username = username,
            userEmail = email
        )
    }

    private fun buildSignInRequest():BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}