
package com.final_year_project.kisaan10

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.final_year_project.kisaan10.auth.LoginScreen
import com.final_year_project.kisaan10.auth.SignUpScreen
import com.final_year_project.kisaan10.auth.googleAuth.GoogleAuthUiClient
import com.final_year_project.kisaan10.auth.googleAuth.SignInViewModel
import com.final_year_project.kisaan10.auth.showToast
import com.final_year_project.kisaan10.auth.validateSignUp
import com.final_year_project.kisaan10.pages.HomeScreen
import com.final_year_project.kisaan10.splash.SplashScreen
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalUnitApi
class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy { 
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Kisaan10Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen()
                        LaunchedEffect(key1 = true) {
                            delay(3000) // 3 seconds delay
                            navController.navigate("signup") {
                                popUpTo("splash") {
                                    inclusive = true
                                }
                            }
                        }
                    }

                    composable("login") {
                        LoginScreen(
                            onLoginClicked = { username, password ->
                                val uname = username
                                val upas = password
                                Toast.makeText(
                                    this@MainActivity,
                                    "Username: $uname\nPassword: $upas",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            signUpNavigation = {
                                navController.navigate("signup") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable("signup") {
                        val viewModel = viewModel<SignInViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit) {
                            if (googleAuthUiClient.getSignedInUser() != null) {
                                navController.navigate("home") {
                                    popUpTo("signup") {
                                        inclusive = true
                                    }
                                }
                            }
                        }

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult()
                        ) { result ->
                            if (result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    viewModel.onSignInResult(signInResult)
                                }
                            }
                        }

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if (state.isSignInSuccessful) {
                                showToast(applicationContext, "I am Done")
                                navController.navigate("home")
                                viewModel.resetState()
                            }
                        }

                        SignUpScreen(
                            onSignUpClicked = { username, email, password, confirmPassword, ->
                                val validationError = validateSignUp(username, email, password, confirmPassword)
                                if (validationError != null) {
                                    showToast(this@MainActivity, validationError)


                                } else {
                                    // Proceed with sign-up logic
                                    // showToast(this@MainActivity, "Hello $username")
                                    // Call your signUp function here
                                }
                            },
                            signInNavigation = {
                                navController.navigate("login") {
                                    popUpTo("signup") {
                                        inclusive = true
                                    }
                                }
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

                    composable("home") {
                        HomeScreen(
                            userData = googleAuthUiClient.getSignedInUser(),
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthUiClient.signOut()
                                    showToast(applicationContext, "Signed Out")
                                    navController.navigate("signup") {
                                        popUpTo("home") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        )
                    }
                }

//                val homeNavController = rememberNavController()
//                NavHost(navController = homeNavController, startDestination = "home"){
//
//                }

            }

        }
}}
