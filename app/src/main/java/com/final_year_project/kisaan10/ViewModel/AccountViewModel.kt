//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import com.google.android.gms.tasks.Task
//import com.google.android.gms.tasks.Tasks
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.tasks.await
//
//data class UserData(
//    val userId: String,
//    val username: String?,
//    val userEmail: String?
//)
//
//class AccountViewModel : ViewModel() {
//    private val auth = FirebaseAuth.getInstance()
//    private val db = FirebaseFirestore.getInstance()
//
//    private val _userData = MutableStateFlow<UserData?>(null)
//    val userData: StateFlow<UserData?> = _userData
//
//    companion object {
//        private const val USERS_COLLECTION = "users"
//        private const val TAG = "AccountViewModel"
//    }
//
//    init {
//        fetchUserData()
//    }
//
//    /**
//     * Fetches user data from Firestore and updates the _userData state.
//     */
//    private fun fetchUserData() {
//        val currentUser = auth.currentUser
//        currentUser?.let { user ->
//            db.collection(USERS_COLLECTION).document(user.uid).get()
//                .addOnSuccessListener { document ->
//                    val userData = document.toObject(UserData::class.java)
//                    _userData.value = userData?.copy(userId = user.uid)
//                }
//                .addOnFailureListener { e ->
//                    Log.e(TAG, "Error fetching user data", e)
//                }
//        }
//    }
//
//    /**
//     * Updates user data in Firestore and Firebase Authentication.
//     *
//     * @param userId The user's ID.
//     * @param username The new username.
//     * @param email The new email.
//     * @param password The new password.
//     * @param onResult Callback to handle the result of the update.
//     */
//    fun updateUserData(userId: String, username: String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
//        val user = auth.currentUser
//
//        if (user == null) {
//            onResult(false, "User not logged in")
//            return
//        }
//
//        // Update Firestore user data
//        val userData = UserData(userId, username, email)
//        db.collection(USERS_COLLECTION).document(userId)
//            .set(userData)
//            .addOnSuccessListener {
//                val tasks = mutableListOf<Task<Void>>()
//
//                if (user.email != email) {
//                    tasks.add(user.updateEmail(email))
//                }
//                if (password.isNotEmpty()) {
//                    tasks.add(user.updatePassword(password))
//                }
//
//                Tasks.whenAllComplete(tasks)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            // Refresh Firebase user data
//                            user.reload().addOnCompleteListener {
//                                fetchUserData() // Refresh user data
//                                onResult(true, null)
//                            }
//                        } else {
//                            onResult(false, task.exception?.message)
//                        }
//                    }
//            }
//            .addOnFailureListener { e ->
//                onResult(false, e.message)
//            }
//    }
//    private suspend fun getUserUsernameFromFirestore(email: String): String? {
//        val usersCollection = FirebaseFirestore.getInstance().collection("users")
//        val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
//        return if (!querySnapshot.isEmpty) {
//            querySnapshot.documents[0].getString("name")
//        } else {
//            null
//        }
//    }
//    suspend fun getSignedInUser(): com.final_year_project.kisaan10.auth.googleAuth.UserData? {
//        return auth.currentUser?.run {
//            var username = displayName ?: getUserUsernameFromFirestore(email!!)
//            if (displayName==null || displayName.toString()==""){
//                username = getUserUsernameFromFirestore(email!!)
//            }
//            com.final_year_project.kisaan10.auth.googleAuth.UserData(
//                userId = uid,
//                username = username,
//                userEmail = email
//            )
//        }
//    }
//}
