
package com.final_year_project.kisaan10

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.final_year_project.kisaan10.auth.LoginScreen
import com.final_year_project.kisaan10.auth.SignUpScreen
import com.final_year_project.kisaan10.auth.showToast
import com.final_year_project.kisaan10.splash.SplashScreen
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme
import kotlinx.coroutines.delay

@ExperimentalUnitApi
class MainActivity : ComponentActivity() {
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
                            navController.navigate("login") {
                                // Pop up back stack to ensure that splash screen is removed from the back stack
                                popUpTo("splash") {
                                    inclusive = true
                                }
                            }

                        }
                    }

                    composable("login") {

                        LoginScreen(onLoginClicked = { username, password ->
                            run {
                                val uname = username
                                val upas = password
                                Toast.makeText(
                                    this@MainActivity,
                                    "Username: $uname\nPassword: $upas",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                            signUpNavigation = {
                                navController.navigate("signup"){
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable("signup") {
                        SignUpScreen(onSignUpClicked = {username,email, password,confirmPassword->{
                            showToast(this@MainActivity, "Hello $username")
                        } },
                            signInNavigation = {
                                navController.navigate("login"){
                                    popUpTo("signup") {
                                        inclusive = true
                                    }}
                            })
                    }

                }

//                val homeNavController = rememberNavController()
//                NavHost(navController = homeNavController, startDestination = "home"){
//
//                }

            }

        }
}}
