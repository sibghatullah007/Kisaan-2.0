package com.final_year_project.kisaan10.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.final_year_project.kisaan10.auth.googleAuth.SignInViewModel


@Composable
fun SettingScreen(
) {
    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val user = viewModel.dataa
     Log.v("myData", user.toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(text = "Home Screen", fontSize = 20.sp)
        if (user!= null) {
            Text(text = user,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
//        Button(onClick = { onSignOut }) {
//            Text(text = "Sign out")
//        }
    }
}

@Preview
@Composable
fun Preview(){
 SettingScreen()
}