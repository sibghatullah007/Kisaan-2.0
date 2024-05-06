package com.final_year_project.kisaan10.model


import com.google.firebase.Timestamp

data class UserInfo(val name: String = "",
                    val email: String = "",
                    val createdAt: Timestamp = Timestamp.now(),
//                    val location: Location? = Location(),
//                    val preferences: Preferences? = Preferences(),
//                    val otherUserInfo: Any? = Any())
//data class Location(
//    val latitude: Double = 0.0,
//    val longitude: Double = 0.0,
//    val address: String = ""
//)

//data class Preferences(
//    val language: String = "",
//    val notifications: Boolean = false,
//    val cropPreferences: Any = Any()
)