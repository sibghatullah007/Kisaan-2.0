package com.final_year_project.kisaan10.auth.googleAuth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)
