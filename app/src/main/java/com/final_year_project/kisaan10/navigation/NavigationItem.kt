package com.final_year_project.kisaan10.navigation

import androidx.compose.ui.graphics.vector.ImageVector

//-------------- Navigation Item on Bottom Nav Bar---------------------//

data class NavigationItem(
    val title : String,
    val route : String,
    val selectedIcon : ImageVector,
    val unSelectedIcon : ImageVector,
    val hasBadgeDot: Boolean = false,
    val badgeCount : Int? = null
)