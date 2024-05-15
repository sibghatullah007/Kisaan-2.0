package com.final_year_project.kisaan10.auth.googleAuth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel(){
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult){

        _state.update {it.copy(
                isSignInSuccessful = true,
                signInErrorMessage = result.errorMessage

                )}
    }

    fun resetState(){
        _state.update { SignInState() }
    }
}