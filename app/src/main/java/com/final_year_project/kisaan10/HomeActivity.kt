package com.final_year_project.kisaan10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme
import kotlinx.coroutines.delay

class HomeActivity : ComponentActivity() {
    // Define your screens
    sealed class BottomNavScreen(val route: String) {
        object Home : BottomNavScreen("home")
        object Left : BottomNavScreen("left")
        object Right : BottomNavScreen("right")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navItems = listOf(BottomNavScreen.Home, BottomNavScreen.Left, BottomNavScreen.Right)
            var currentScreen by remember { mutableStateOf<BottomNavScreen>(value = BottomNavScreen.Home) }
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        navItems.forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(Icons.Filled.Home,contentDescription = null)}, // Replace ic_home
                                label = { Text(screen.route) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    currentScreen = screen
                                    navController.navigate(screen.route) {
                                        // Optional for clearing back stack:
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavigationHost(navController, currentScreen.route, innerPadding)
            }
        }
        }

    }
@Composable
fun NavigationHost(navController: NavHostController, startDestination: String, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(HomeActivity.BottomNavScreen.Home.route) { HomeScreen() }
        composable(HomeActivity.BottomNavScreen.Left.route) { LeftScreen() }
        composable(HomeActivity.BottomNavScreen.Right.route) { RightScreen() }
    }
}