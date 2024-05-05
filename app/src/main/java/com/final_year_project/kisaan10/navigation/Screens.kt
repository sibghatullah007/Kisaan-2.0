package com.final_year_project.kisaan10.navigation
sealed class Screens(var route: String) {

    object  Home : Screens("home")
    object  Blog : Screens("blog")
    object  Notification : Screens("notification")
    object  Setting : Screens("setting")
}