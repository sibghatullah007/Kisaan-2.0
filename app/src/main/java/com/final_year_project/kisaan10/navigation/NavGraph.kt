package com.final_year_project.kisaan10.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.final_year_project.kisaan10.auth.googleAuth.UserData
import com.final_year_project.kisaan10.screens.BlogScreen
import com.final_year_project.kisaan10.screens.HomeScreen
import com.final_year_project.kisaan10.screens.NotificationScreen
import com.final_year_project.kisaan10.screens.SettingScreen


@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(navController = navController,
        startDestination = Screens.Home.route){
        composable(Screens.Home.route){
            HomeScreen()
        }
        composable(Screens.Notification.route){
            NotificationScreen()
        }
        composable(Screens.Blog.route){
            BlogScreen()
        }

        composable(Screens.Setting.route){
            SettingScreen()
        }
    }
}