package com.final_year_project.kisaan10.bottom_nav_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon:ImageVector

)
val Items = listOf(
    BottomNavigationItem(
        title = "Home",
        unSelectedIcon = Icons.Filled.Home,
        selectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        title = "Accounts",
        unSelectedIcon = Icons.Filled.AccountCircle,
        selectedIcon = Icons.Outlined.AccountCircle
    ),
    BottomNavigationItem(
        title = "Diagnose",
        unSelectedIcon = Icons.Filled.PhotoCamera,
        selectedIcon = Icons.Outlined.PhotoCamera
    )
)
