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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.final_year_project.kisaan10.auth.googleAuth.validateSignIn
import com.final_year_project.kisaan10.auth.googleAuth.validateSignUp
import com.final_year_project.kisaan10.screens.components.Devider
import com.final_year_project.kisaan10.screens.components.ScreenTextFeild
import com.final_year_project.kisaan10.screens.components.WithIcons
import com.final_year_project.kisaan10.screens.components.showToast
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme

val textFieldPadding = 32.dp
val cornerRadius = 25.dp

@Composable
fun SignUpScreen(onSignUpClicked:(String,String,String,String, (Boolean) -> Unit)->Unit,
                 signInNavigation:()->Unit,
                 signUpWithGoogle:()->Unit,
                 state: SignInState,
                 loading: Boolean,
                 setLoading: (Boolean) -> Unit,
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
                .background(color = MaterialTheme.colorScheme.background)
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
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(modifier = Modifier
                    .padding(top = 4.dp),
                    text ="Sign up to get started",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                        fontSize = 18.sp,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onBackground
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
//                    showToast(context = context, message = "Click: Button")
                        val validateSignUp = validateSignUp(userName,userEmail,userPassword,confirmUserPassword)
                        if (validateSignUp == null) {
                            onSignUpClicked.invoke(userName,userEmail,userPassword,confirmUserPassword,setLoading)
                        } else {
                            com.final_year_project.kisaan10.screens.components.showToast(context, validateSignUp)
                        }
                    }) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = Color.White
                            )
                        } else {
                            Text(
                                text ="Sign Up",
                                style = androidx.compose.ui.text.TextStyle(
                                    fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.height(50.dp)
                ) {
                    Devider()
                    Text(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                        text =  "Or Sign up with",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                            fontSize = 16.sp,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onBackground
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
//                    WithIcons(
//                        iconRes = R.drawable.fb,
//                        contentDescription = "Sign up with Facebook",
//                        context = context,
//                        onClick = signUpWithGoogle,
//                    )
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
                        color = MaterialTheme.colorScheme.onBackground,
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




//@Composable
//fun WithIcons(iconRes: Int, contentDescription: String, context:Context, onClick: () -> Unit){
//    OutlinedButton(
//        modifier = Modifier
//            .height(46.dp)
//            .width(230.dp),
//        shape = CircleShape,
//        contentPadding = PaddingValues(6.dp),
//        border = BorderStroke(0.15.dp, Color.Gray),
////        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
//        onClick = onClick) {
//        Text(text = contentDescription
//            , style = androidx.compose.ui.text.TextStyle(
//                fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
//                fontSize = 15.sp,
//                letterSpacing = 1.sp,
//                color = MaterialTheme.colorScheme.primary
//            ) )
//        Icon(
//            painterResource(id = iconRes),
//            contentDescription = contentDescription,
//            tint = Color.Unspecified,
//        )
//    }
//}
//@Composable
//fun Devider(){
//    HorizontalDivider(
//        modifier = Modifier.width(64.dp),
//        thickness = 1.dp,
//        color = Color(0xFF333333)
//    )
//}
//
//@Composable
//fun ScreenTextFeild(
//    text: String,
//    hint: String,
//    leadingIcon: ImageVector,
//    password:Boolean,
//    onText: (String)->Unit,
//){
//    var passwordHidden by rememberSaveable{ mutableStateOf(true) }
//    OutlinedTextField(
//        visualTransformation =  if (password) {
//            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None
//        } else VisualTransformation.None,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(
//                start = textFieldPadding,
//                end = textFieldPadding,
//                top = textFieldPadding,
//            )
//            .background(Color.White, RoundedCornerShape(cornerRadius)),
//        value = text,
//        colors = OutlinedTextFieldDefaults.colors(
//            cursorColor = MaterialTheme.colorScheme.primary,
//            focusedBorderColor = MaterialTheme.colorScheme.primary,
//            unfocusedBorderColor = Color.White,
//        ),
//        onValueChange = { onText(it) },
//        singleLine = true,
//        shape = RoundedCornerShape(cornerRadius),
//        textStyle = screenTextField(MaterialTheme.colorScheme.primary),
//        placeholder = {
//            Text(text = hint,
//                style = screenTextField(Color(0xFF808080))
//            )
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = leadingIcon,
//                contentDescription = hint,
//                tint = MaterialTheme.colorScheme.primary
//            )
//        },
//        keyboardOptions =
//        KeyboardOptions(keyboardType = if (password) {
//            KeyboardType.Password
//        } else KeyboardType.Text),
//
//        trailingIcon = {
//            if (password){
//                IconButton(onClick = { passwordHidden = !passwordHidden }) {
//                    val visibilityIcon =
//                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
//                    val description = if (passwordHidden) "Show password" else "Hide password"
//                    Icon(imageVector = visibilityIcon, contentDescription = description)}
//            }
//        }
//    )
//
//
//}
//
//@Composable
//fun screenTextField(textColor:Color) = androidx.compose.ui.text.TextStyle(
//    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
//    letterSpacing = 1.sp,
//    color = textColor
//)
//
//fun showToast(context:Context, message:String){
//    Toast.makeText(
//        context.applicationContext, message,
//        Toast.LENGTH_SHORT
//    ).show()
//}

//@Preview
//@Composable
//fun SignUpScreenPreview() {
//    SignUpScreen(onSignUpClicked = {_,_,_,_->/*   */},
//        signInNavigation = {}, signUpWithGoogle = {}, SignInState(),    )
//}