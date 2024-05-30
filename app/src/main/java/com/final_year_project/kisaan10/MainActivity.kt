package com.final_year_project.kisaan10

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yourapp.AppNotificationManager
import com.final_year_project.kisaan10.ViewModel.BlogsViewModel
import com.final_year_project.kisaan10.ViewModel.ImageSelectionViewModel
import com.final_year_project.kisaan10.ViewModel.RecentDiseaseViewModel
import com.final_year_project.kisaan10.ViewModel.WheatViewModel
import com.final_year_project.kisaan10.auth.googleAuth.GoogleAuthUiClient
import com.final_year_project.kisaan10.auth.googleAuth.SignInViewModel
import com.final_year_project.kisaan10.auth.googleAuth.UserData
import com.final_year_project.kisaan10.auth.googleAuth.validateSignUp
import com.final_year_project.kisaan10.screens.LoginScreen
import com.final_year_project.kisaan10.screens.MainScreen
import com.final_year_project.kisaan10.screens.SignUpScreen
import com.final_year_project.kisaan10.screens.SplashScreen
import com.final_year_project.kisaan10.screens.components.ForgotPasswordScreen
import com.final_year_project.kisaan10.screens.components.showToast
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val Maincontext = this
    private lateinit var auth: FirebaseAuth
    //Blogs ViewModel
    private val blogsViewModel: BlogsViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(BlogsViewModel::class.java)
    }
    //Recent Disease ViewModel
    private val recentDiseaseViewModel: RecentDiseaseViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(RecentDiseaseViewModel::class.java)
    }
//  Wheat ViewModel
    private val wheatViewModel: WheatViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(WheatViewModel::class.java)
    }
    //
    private val imageSelectionViewModel : ImageSelectionViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(ImageSelectionViewModel::class.java)
    }
//  private lateinit var database: KissanDatabase


    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        //Testing Database
//        database = KissanDatabase.getDatabase(this)
//
//            val data = database.blogsDAO().getBlogs().observe(this@MainActivity) {
//                Log.d("database", it.toString())
//            }

        initializeFirebase()
        setContent {
            Kisaan10Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("signup") { SignUpScreen(navController) }
                    composable("home") { HomeScreen(navController) }
                    composable("forgetPassword") { ForgetPassword(navController) }

                }
            }
        }
        AppNotificationManager.createNotificationChannel(this)
        requestNotificationPermission()
    }


    private fun initializeFirebase() {
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        FirebaseManager.init(this)
    }

    @Composable
    fun SplashScreen(navController: NavController) {

        LaunchedEffect(key1 = Unit) {

            if (googleAuthUiClient.getSignedInUser() != null) {
                delay(3000)
                navController.navigate("home") { popUpTo("splash") { inclusive = true } }
            }else{
                delay(3000)
                navController.navigate("signup") { popUpTo("splash") { inclusive = true }}
            }
        }
        SplashScreen()
    }

    @Composable
    fun LoginScreen(navController: NavController) {
        val viewModel = viewModel<SignInViewModel>()
        LoginScreen(
            onForgetPassClicked = {
                navController.navigate("forgetPassword") { popUpTo("login") { inclusive = true } }
            },
            onLoginClicked = { username, password ->
                showToast(this@MainActivity, "Username: $username\nPassword: $password")
                lifecycleScope.launch {
                    googleAuthUiClient.signInWithEmailPassword(username, password).let { signInResult ->
                        viewModel.onSignInResult(signInResult)
                        if (signInResult.data != null) {
                            Log.d("SignIn", "User signed in successfully: ${signInResult.data}")
                            navController.navigate("home") { popUpTo("login") { inclusive = true } }
                        } else {
                            Log.e("SignIn", "Failed to sign in user: ${signInResult.errorMessage}")
                        }
                    }
                }
            },
            signUpNavigation = {
                navController.navigate("signup") { popUpTo("login") { inclusive = true } }
            }
        )
    }

    @Composable
    fun SignUpScreen(navController: NavController) {
        val viewModel = viewModel<SignInViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(result.data ?: return@launch)
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (state.isSignInSuccessful) {
                showToast(applicationContext, "I am Done")
                navController.navigate("home") { popUpTo("signup") { inclusive = true } }
                viewModel.resetState()
            }
        }


        SignUpScreen(
            onSignUpClicked = { username, email, password, confirmPassword ->
                val validationError = validateSignUp(username, email, password, confirmPassword)
                if (validationError != null) {
                    showToast(this@MainActivity, validationError)
                } else {
                    lifecycleScope.launch {
                        googleAuthUiClient.signUpUser(username, email, password) { success, exception ->
                            if (success) {
                                showToast(this@MainActivity, "Successful")
                                navController.navigate("home") { popUpTo("signup") { inclusive = true } }
                                AppNotificationManager.sendNotification(
                                    context = this@MainActivity,
                                    "Welcome to Kisaan App",
                                    "Version 1.2 is now available with new features"
                                )
                            } else {
                                exception?.let { showToast(this@MainActivity, exception.toString()) }
                            }
                        }
                    }
                }
            },
            signInNavigation = {
                navController.navigate("login") { popUpTo("signup") { inclusive = true } }
            },
            signUpWithGoogle = {
                lifecycleScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            },

            state = state,
        )
    }

    @Composable
    fun HomeScreen(navController: NavController) {
        var userData by remember { mutableStateOf<UserData?>(null) }

        LaunchedEffect(Unit) {
            userData = googleAuthUiClient.getSignedInUser()
        }

        MainScreen(
            recentDiseaseViewModel,
            imageSelectionViewModel,
            blogsViewModel,
            wheatViewModel,
            userData = userData,
            onSignOut = {
//                val context = applicationContext
                val activityContext = this // Assuming MainActivity is your activity
                activityContext.let {
                    AlertDialog.Builder(it)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Logout") { dialog, _ ->
                            lifecycleScope.launch {
                                googleAuthUiClient.signOut()
                                showToast(it, "Signed Out")
                                navController.navigate("signup") { popUpTo("home") { inclusive = true } }
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                            dialog.dismiss()
                        })
                        .show()
                }
            }
        )
    }

    @Composable
    fun ForgetPassword(navController: NavController){
        
        ForgotPasswordScreen(
            onResetPasswordClicked = {
               auth.sendPasswordResetEmail(it).addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       Toast.makeText(Maincontext, "Password reset email sent", Toast.LENGTH_LONG).show()
                   } else {
                       Toast.makeText(Maincontext, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                   }
               }
            },
            loginNavigation = {
                navController.navigate("login") { popUpTo("forgetPassword") { inclusive = true } }
            }
        )
        
    }





    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
    }
}




















//
//package com.final_year_project.kisaan10
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.result.IntentSenderRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.final_year_project.kisaan10.ViewModel.UserViewModel
//import com.final_year_project.kisaan10.auth.EmailAuth.signUpUser
//import com.final_year_project.kisaan10.screens.LoginScreen
//import com.final_year_project.kisaan10.screens.SignUpScreen
//import com.final_year_project.kisaan10.auth.googleAuth.GoogleAuthUiClient
//import com.final_year_project.kisaan10.auth.googleAuth.SignInViewModel
//import com.final_year_project.kisaan10.auth.googleAuth.UserData
//import com.final_year_project.kisaan10.auth.googleAuth.validateSignUp
//import com.final_year_project.kisaan10.screens.components.showToast
//import com.final_year_project.kisaan10.screens.MainScreen
//import com.final_year_project.kisaan10.screens.SplashScreen
//import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme
//import com.google.firebase.Firebase
//import com.google.firebase.FirebaseApp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.auth
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class MainActivity : ComponentActivity() {
//    private val googleAuthUiClient by lazy {
//        GoogleAuthUiClient(
//            context = applicationContext,
//            oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
//        )
//    }
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //FireStore
//        FirebaseManager.init(this)
//        // In your Application class or MainActivity
//        FirebaseApp.initializeApp(this)
//        // Initialize Firebase Auth
//        auth = Firebase.auth
//
//
//        setContent {
//            Kisaan10Theme {
//                val navController = rememberNavController()
//
//                NavHost(navController = navController, startDestination = "splash") {
//                    composable("splash") {
//                        val viewModel = viewModel<SignInViewModel>()
//                        val state by viewModel.state.collectAsStateWithLifecycle()
//
//                        LaunchedEffect(key1 = Unit) {
//                            if (googleAuthUiClient.getSignedInUser() != null) {
//                                navController.navigate("home") {
//                                    popUpTo("splash") {
//                                        inclusive = true
//                                    }
//                                }
//                            }
//                        }
//
//                        LaunchedEffect(key1 = state.isSignInSuccessful) {
//                            if (state.isSignInSuccessful) {
//                                showToast(applicationContext, "I am Done")
//                                delay(2500)
//                                navController.navigate("home")
//                                viewModel.resetState()
//                            }
//                        }
//
//                        SplashScreen()
//                        LaunchedEffect(key1 = true) {
//                            delay(3000) // 3 seconds delay
//                            navController.navigate("signup") {
//                                popUpTo("splash") {
//                                    inclusive = true
//                                }
//                            }
//                        }
//                    }
//
//
//
//
//
//                    composable("login") {
//                        val viewModel = viewModel<SignInViewModel>()
//                        val state by viewModel.state.collectAsState()
//
//                        val launcher = rememberLauncherForActivityResult(
//                            contract = ActivityResultContracts.StartIntentSenderForResult()
//                        ) { result ->
//                            if (result.resultCode == RESULT_OK) {
//                                lifecycleScope.launch {
//                                    val signInResult = googleAuthUiClient.signInWithIntent(
//                                        intent = result.data ?: return@launch
//                                    )
//                                    viewModel.onSignInResult(signInResult)
//                                }
//                            }
//                        }
//                        LoginScreen(
//                            onLoginClicked = { username, password ->
//                                val uname = username
//                                val upas = password
//                                Toast.makeText(
//                                    this@MainActivity,
//                                    "Username: $uname\nPassword: $upas",
//                                    Toast.LENGTH_SHORT
//                                ).show()
////                                auth.signInWithEmailAndPassword(username, password)
////                                    .addOnCompleteListener() { task ->
////                                        if (task.isSuccessful) {
//
//
//                                lifecycleScope.launch(){
//                                    googleAuthUiClient.signInWithEmailPassword(uname, upas)
//                                        .let { signInResult ->
//                                            viewModel.onSignInResult(signInResult)
//                                            if (signInResult.data != null) {
//                                                // User signed in successfully
//                                                val userData = signInResult.data
//
//
//                                                Log.d(
//                                                    "SignIn",
//                                                    "User signed in successfully: $userData"
//                                                )
//                                                navController.navigate("home") {
//                                                    popUpTo("login") {
//                                                        inclusive = true
//                                                    }
//                                                }
//                                            } else {
//                                                // Failed to sign in user
//                                                val errorMessage = signInResult.errorMessage
//                                                Log.e(
//                                                    "SignIn",
//                                                    "Failed to sign in user: $errorMessage"
//                                                )
//                                            }
//                                        }
//                                }
//                                            // Sign in success, update UI with the signed-in user's information
//
////                                        } else {
////                                            // If sign in fails, display a message to the user.
////                                            showToast(this@MainActivity,"Something went wrong")
////                                        }
////                                    }
//                            },
//                            signUpNavigation = {
//                                navController.navigate("signup") {
//                                    popUpTo("login") {
//                                        inclusive = true
//                                    }
//                                }
//                            }
//                        )
//                    }
//
//                    composable("signup") {
//                        val viewModel = viewModel<SignInViewModel>()
//                        val state by viewModel.state.collectAsStateWithLifecycle()
////
////                        LaunchedEffect(key1 = Unit) {
////                            if (googleAuthUiClient.getSignedInUser() != null) {
////                                navController.navigate("home") {
////                                    popUpTo("signup") {
////                                        inclusive = true
////                                    }
////                                }
////                            }
////                        }
////
//                        val launcher = rememberLauncherForActivityResult(
//                            contract = ActivityResultContracts.StartIntentSenderForResult()
//                        ) { result ->
//                            if (result.resultCode == RESULT_OK) {
//                                lifecycleScope.launch {
//                                    val signInResult = googleAuthUiClient.signInWithIntent(
//                                        intent = result.data ?: return@launch
//                                    )
//                                    viewModel.onSignInResult(signInResult)
//                                }
//                            }
//                        }
////
////                        LaunchedEffect(key1 = state.isSignInSuccessful) {
////                            if (state.isSignInSuccessful) {
////                                showToast(applicationContext, "I am Done")
////                                navController.navigate("home")
////                                viewModel.resetState()
////                            }
////                        }
//
//                        SignUpScreen(
//                            onSignUpClicked = { username, email, password, confirmPassword ->
//                                val validationError = validateSignUp(username, email, password, confirmPassword)
//                                if (validationError != null) {
//                                    showToast(this@MainActivity, validationError)
//                                } else {
//                                    // Proceed with sign-up logic
//                                    // showToast(this@MainActivity, "Hello $username")
//                                    // Call your signUp function here
//                                    // Example call
//                                    lifecycleScope.launch(){
//                                        googleAuthUiClient.signUpUser(
//                                            username,
//                                            email,
//                                            password
//                                        ) { success, exception ->
//                                            if (success) {
//                                                // User signed up successfully
//                                                showToast(this@MainActivity, "Successful")
//                                                navController.navigate("home") {
//                                                    popUpTo("signup") {
//                                                        inclusive = true
//                                                    }
//                                                }
//                                            } else {
//                                                // Failed to sign up user, handle exception
//                                                exception?.let {
//                                                    // Handle exception
//                                                    showToast(this@MainActivity, "UnSuccessful")
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                }
//                            },
//                            signInNavigation = {
//                                navController.navigate("login") {
//                                    popUpTo("signup") {
//                                        inclusive = true
//                                    }
//                                }
//                            },
//                            signUpWithGoogle = {
//                                lifecycleScope.launch {
//                                    val signInIntentSender = googleAuthUiClient.signIn()
//                                    launcher.launch(
//                                        IntentSenderRequest.Builder(
//                                            signInIntentSender ?: return@launch
//                                        ).build()
//                                    )
//                                }
//                            },
//                            state = state,
//                        )
//                    }
//
//                    composable("home") {
////                        HomeScreen(
////                        userData = googleAuthUiClient.getSignedInUser(),
////                        onSignOut = {
////                            lifecycleScope.launch {
////                                googleAuthUiClient.signOut()
////                                showToast(applicationContext, "Signed Out")
////                                navController.navigate("signup") {
////                                    popUpTo("home") {
////                                        inclusive = true
////                                    }
////                                }
////                            }
////                        }
////                        )
//                        var userData by remember { mutableStateOf<UserData?>(null) }
//                        LaunchedEffect(Unit) {
//                            userData = googleAuthUiClient.getSignedInUser()
//                        }
//                        MainScreen(
//                            userData = userData,
//                            onSignOut = {
//                                lifecycleScope.launch {
//                                    googleAuthUiClient.signOut()
//                                    showToast(applicationContext, "Signed Out")
//                                    navController.navigate("signup") {
//                                        popUpTo("home") {
//                                            inclusive = true
//                                        }
//                                    }
//                                }
//                            }
//                        )
//                    }
//                }
//
////                val homeNavController = rememberNavController()
////                NavHost(navController = homeNavController, startDestination = "home"){
////
////                }
//
//            }
//
//        }
//}}
