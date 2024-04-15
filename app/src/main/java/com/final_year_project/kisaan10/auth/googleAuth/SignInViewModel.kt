package com.final_year_project.kisaan10.auth.googleAuth

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel(){
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    private var daataa = UserData("","")
    public  var dataa:String? = "a"
    fun onSignInResult(result: SignInResult){
        daataa.userId = result.data!!.userId
        daataa.username = result.data.username
        dataa = daataa.username
        daataa.username?.let { Log.v("ourData", it) }
        _state.update {it.copy(
                isSignInSuccessful = result.data != null,
                signInErrorMessage = result.errorMessage

                )}
    }

    fun resetState(){
        _state.update { SignInState() }
    }
}