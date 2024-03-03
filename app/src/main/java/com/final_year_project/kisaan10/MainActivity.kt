
package com.final_year_project.kisaan10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@ExperimentalUnitApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    SplashScreen()
                    LaunchedEffect(key1 = true) {
                        delay(3000) // 3 seconds delay
                        navController.navigate("main_screen") {
                            // Pop up back stack to ensure that splash screen is removed from the back stack
                            popUpTo("splash") {
                                inclusive = true
                            }
                        }

                    }
                }

                    composable("main_screen") {
                        Text(text = "Hello")
                    }
                }
            }
        }
}
