package com.final_year_project.kisaan10.auth

fun validateSignUp(username: String, email: String, password: String, confirmPassword: String): String? {
    // Check if username is empty
    if (username.isEmpty()) {
        return "Username cannot be empty"
    }

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

    // Check if confirmPassword is empty
    if (confirmPassword.isEmpty()) {
        return "Please confirm your password"
    }

    // Check if password and confirmPassword match
    if (password != confirmPassword) {
        return "Passwords do not match"
    }

    // If all validations pass, return null indicating no errors
    return null
}