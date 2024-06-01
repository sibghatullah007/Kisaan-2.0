package com.final_year_project.kisaan10.auth.googleAuth

fun validateSignIn(email: String, password: String): String? {

    // Check if email is empty
    if (email.isEmpty()) {
        return "Email cannot be empty"
    }

    // Check if email is valid
    val emailRegex = Regex(pattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    if (!email.matches(emailRegex)) {
        return "Invalid email format"
    }

    // Check if password is empty
    if (password.isEmpty()) {
        return "Password cannot be empty"
    }

    // Check if password length is less than 6 characters
    if (password.length < 6) {
        return "Password must be at least 6 characters long"
    }

    // If all validations pass, return null indicating no errors
    return null
}