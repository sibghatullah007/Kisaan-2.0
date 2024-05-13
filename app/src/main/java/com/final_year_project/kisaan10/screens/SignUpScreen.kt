package com.final_year_project.kisaan10.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.auth.googleAuth.SignInState
import com.final_year_project.kisaan10.screens.components.Devider
import com.final_year_project.kisaan10.screens.components.ScreenTextFeild
import com.final_year_project.kisaan10.screens.components.WithIcons
import com.final_year_project.kisaan10.screens.components.showToast
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme

val textFieldPadding = 32.dp
val cornerRadius = 25.dp

@Composable
fun SignUpScreen(onSignUpClicked:(String,String,String,String)->Unit,
                 signInNavigation:()->Unit,
                 signUpWithGoogle:()->Unit,
                 state: SignInState,
                 context: Context = LocalContext.current) {


    Kisaan10Theme {

        LaunchedEffect(key1 = state.signInErrorMessage) {
            state.signInErrorMessage?.let{
                    error ->
                showToast(context, error)
            }
        }
        var userName by rememberSaveable {
            mutableStateOf("")
        }
        var userEmail by rememberSaveable {
            mutableStateOf("")
        }
        var userPassword by rememberSaveable {
            mutableStateOf("")
        }
        var confirmUserPassword by rememberSaveable {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onBackground)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(modifier = Modifier
                    .padding(top = 24.dp),
                    text = "Create Account",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                        fontSize = 30.sp,
                        letterSpacing = 1.sp,
                        color = Color.Black
                    )
                )
                Text(modifier = Modifier
                    .padding(top = 4.dp),
                    text ="Sign up to get started",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                        fontSize = 18.sp,
                        letterSpacing = 1.sp,
                        color = Color.Black
                    )
                )

                ScreenTextFeild(text = userName,
                    hint = "Full Name",
                    leadingIcon = Icons.Outlined.Person,
                    false){
                    userName = it
                }
                ScreenTextFeild(text = userEmail,
                    hint = "Enter Email",
                    leadingIcon = Icons.Outlined.Email,
                    false){
                    userEmail = it
                }
                ScreenTextFeild(text = userPassword,
                    hint = "Enter Password",
                    leadingIcon = Icons.Outlined.Lock,
                    true){
                    userPassword = it
                }
                ScreenTextFeild(text = confirmUserPassword,
                    hint = "Re-Enter Password",
                    leadingIcon = Icons.Outlined.Lock,
                    true){
                    confirmUserPassword = it
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = textFieldPadding,
                            end = textFieldPadding,
                            top = textFieldPadding
                        ),
                    shape = RoundedCornerShape(cornerRadius),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = {
                        onSignUpClicked.invoke(userName,userEmail,userPassword,confirmUserPassword)
                    }) {
                    Text(
                        text ="Sign Up",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Devider()
                    Text(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                        text =  "Or Sign up with",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                            fontSize = 16.sp,
                            letterSpacing = 1.sp,
                            color = Color.Black
                        )
                    )
                    Devider()
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WithIcons(
                        iconRes = R.drawable.google,
                        contentDescription =  "Sign up with Google  ",
                        context = context,
                        onClick = signUpWithGoogle
                    )
                }
                val textBottom1 = "Already a member? "
                val textBottom2 =  "Sign In"
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textBottom1,
                        color = Color.Black,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp
                    )

                    Text(
                        modifier = Modifier.clickable {
                            signInNavigation()
                        },
                        text = textBottom2,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_bold,
                                weight = FontWeight.Bold
                            )
                        ),
                        fontSize = 16.sp
                    )

                }
            }
        }
    }
}



