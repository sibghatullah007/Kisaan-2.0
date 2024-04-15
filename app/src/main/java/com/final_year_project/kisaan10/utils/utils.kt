package com.final_year_project.kisaan10.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import com.final_year_project.kisaan10.navigation.NavigationItem
import com.final_year_project.kisaan10.navigation.Screens

//import com.meet.bottomnavigationbarjetpackcompose.navigation.NavigationItem
//import com.meet.bottomnavigationbarjetpackcompose.navigation.Screens

val bottomNavigationItemsList = listOf(
    NavigationItem(
        title = "Home",
        route = Screens.Home.route,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
    ),
    NavigationItem(
        title = "Blogs",
        route = Screens.Blog.route,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        unSelectedIcon = Icons.AutoMirrored.Outlined.Article,
    ),
    NavigationItem(
        title = "Notification",
        route = Screens.Notification.route,
        selectedIcon = Icons.Filled.Notifications,
        unSelectedIcon = Icons.Outlined.Notifications,
        badgeCount = 9
    ),
    NavigationItem(
        title = "Setting",
        route = Screens.Setting.route,
        selectedIcon = Icons.Filled.Settings,
        unSelectedIcon = Icons.Outlined.Settings,
        hasBadgeDot = true
    ),
)