package com.final_year_project.kisaan10

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginScreen(onLoginClicked: (String, String) -> Unit,
                onSignUpClicked: () -> Unit) {
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to Kisaan App",
                fontWeight = FontWeight.Bold ,
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier.size(40.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .fillMaxSize()
                    .clip(RectangleShape)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(60.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {  },
                label = { Text(text = "Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onLoginClicked.invoke("username", "password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray, shape = RoundedCornerShape(50.dp)), // Set background color to green
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White), // Set text color to white
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(fontWeight = FontWeight.Bold) // Set font weight to bold
                )
            }
            Spacer(modifier = Modifier.height(25.dp))



                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .height(2.dp) // Width of the vertical line
                            .width(80.dp) // Fill the height of its container
                            .background(Color.Black) // Color of the line
                    )
                    Text(
                        "    OR    ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                    Box(
                        modifier = Modifier
                            .height(2.dp) // Width of the vertical line
                            .width(80.dp) // Fill the height of its container
                            .background(Color.Black) // Color of the line
                    )
                }
            Spacer(modifier = Modifier.height(20.dp))
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    "Not a user?  ",
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp,
                )
                Text(
                    "Sign Up",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.clickable(onClick = onSignUpClicked)
                )
            }

            }
        }
    }

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginClicked = { abc,efg -> /* Dummy lambda for preview */ },
        onSignUpClicked = {})
}