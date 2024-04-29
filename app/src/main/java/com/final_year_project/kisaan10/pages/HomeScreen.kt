package com.final_year_project.kisaan10.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.auth.googleAuth.UserData

@Composable
fun HomeScreen(
    userData: UserData?,
    onSignOut:()->Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(text = "Home Screen", fontSize = 20.sp)
        if (userData?.username != null) {
            Text(text = userData.username!!,
            textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
                )
        }
        Button(onClick = onSignOut) {
            Text(text = "Sign out")
        }
    }
}

//@Preview
//@Composable
//fun preview(){
//    HomeScreen(UserData("A","A"),{})
//
//}
