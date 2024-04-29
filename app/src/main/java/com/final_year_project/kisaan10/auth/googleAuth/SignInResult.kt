package com.final_year_project.kisaan10.auth.googleAuth


data class SignInResult(
    val data: UserData?,
    val errorMessage:String?
)

data class UserData(
    var userId:String,
    var username:String?,
    var userEmail:String?,
)
