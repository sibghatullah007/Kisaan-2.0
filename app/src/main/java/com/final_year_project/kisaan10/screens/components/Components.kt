package com.final_year_project.kisaan10.screens.components

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.auth.googleAuth.validateSignIn
import com.final_year_project.kisaan10.localDB.Blogs
import com.final_year_project.kisaan10.screens.cornerRadius
import com.final_year_project.kisaan10.screens.textFieldPadding
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme
import com.final_year_project.kisaan10.ui.theme.lightGrn

@Composable
fun WithIcons(iconRes: Int, contentDescription: String, context: Context, onClick: () -> Unit){
    OutlinedButton(
        modifier = Modifier
            .height(46.dp)
            .width(230.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(6.dp),
        border = BorderStroke(0.15.dp, Color.Gray),
//        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        onClick = onClick) {
        Text(text = contentDescription
            , style = TextStyle(
                fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.primary
            ) )
        Icon(
            painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = Color.Unspecified,
        )
    }
}
@Composable
fun Devider(){
    HorizontalDivider(
        modifier = Modifier.width(64.dp),
        thickness = 1.dp,
        color = Color(0xFF333333)
    )
}

@Composable
fun ScreenTextFeild(
    text: String,
    hint: String,
    leadingIcon: ImageVector,
    password:Boolean,
    onText: (String)->Unit,
){
    var passwordHidden by rememberSaveable{ mutableStateOf(true) }
    OutlinedTextField(
        visualTransformation =  if (password) {
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None
        } else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = textFieldPadding,
                end = textFieldPadding,
                top = textFieldPadding,
            )
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(cornerRadius)),
        value = text,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
        ),
        onValueChange = { onText(it) },
        singleLine = true,
        shape = RoundedCornerShape(cornerRadius),
        textStyle = screenTextField(MaterialTheme.colorScheme.primary),
        placeholder = {
            Text(text = hint,
                style = screenTextField(Color(0xFF808080))
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = hint,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        keyboardOptions =
        KeyboardOptions(keyboardType = if (password) {
            KeyboardType.Password
        } else KeyboardType.Text),

        trailingIcon = {
            if (password){
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        }
    )


}

@Composable
fun screenTextField(textColor: Color) = androidx.compose.ui.text.TextStyle(
    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
    letterSpacing = 1.sp,
    color = textColor
)

fun showToast(context: Context, message:String){
    Toast.makeText(
        context.applicationContext, message,
        Toast.LENGTH_SHORT
    ).show()
}
@Composable
fun navTextHeading(text: String){
    Text(
        text =text,
        style = androidx.compose.ui.text.TextStyle(
            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.primary
        ),
    )
}

@Composable
fun navTextDescription(text: String){
    Text(
        text =text,
        style = androidx.compose.ui.text.TextStyle(
            fontFamily = FontFamily(Font(R.font.montserrat, FontWeight.Normal)),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
    )
}


@Composable
fun BlogItem(blog: Blogs) {
    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(2.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    val context = LocalContext.current

                    // Remove the "@drawable/" prefix
                    val resourceName = blog.pictureResId.removePrefix("@drawable/")

                    // Get the resource ID dynamically
                    val resourceId = context.resources.getIdentifier(
                        resourceName,
                        "drawable",
                        context.packageName
                    )

                    Image(
                        painter = painterResource(id = resourceId), // Ensure pictureResId is a valid resource ID
                        contentDescription = blog.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = blog.name,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFF333333)
                    )
                }
                item {
                    SectionTitle(text = "Symptoms")
                    Text(
                        text = blog.symptom,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 25.sp,
                        ),
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFF333333)
                    )
                }
                item {
                    SectionTitle(text = if (blog.name == "Healthy Wheat") "Maintenance" else "Treatment")
                    Text(
                        text = blog.treatment,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 25.sp,
                        ),
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFF333333)
                    )
                }
                item {
                    SectionTitle(text = if (blog.name == "Healthy Wheat") "Some Tips for Your Crop" else "Preventions")
                    Text(
                        text = blog.prevention,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 25.sp,
                        ),
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
        @Composable
        fun SectionTitle(text: String) {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.padding(vertical = 15.dp)
            )
        }



@Composable
fun ForgotPasswordScreen(onResetPasswordClicked: (String) -> Unit, loginNavigation: () -> Unit) {
    val context = LocalContext.current
    Kisaan10Theme {
        var userEmail by rememberSaveable {
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
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp),
                    text = "Forgot Password",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                        fontSize = 30.sp,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    text = "Enter your email to reset your password",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                        fontSize = 18.sp,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(25.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                ScreenTextFeild(
                    text = userEmail,
                    hint = "Enter Email",
                    leadingIcon = Icons.Outlined.Email,
                    false
                ) {
                    userEmail = it
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
                        // Check if email is valid
                        val emailRegex = Regex(pattern = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
                        if (userEmail.isEmpty() || !userEmail.matches(emailRegex)) {
                            showToast(context,"Invalid email")
                        }
                        else{
                            onResetPasswordClicked.invoke(userEmail)
                        }
                    }
                ) {
                    Text(
                        text = "Reset Password",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            loginNavigation()
                        },
                        text = "Back to Login",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerEffect() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(cornerRadius))
                .background(brush)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(cornerRadius))
                .background(brush)
        )
    }
}
//Alert dialog to show option to choose Image from Camera or gallery
@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close),
                    contentDescription = "Close",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable(onClick = onCloseClick)
                        .padding(top = 20.dp, end = 20.dp)
                        .size(24.dp),
                )
                    Text(
                        text = "Add image",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 20.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                            fontSize = 18.sp
                        )
                    )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable(onClick = onGalleryClick)
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainer,
                                    RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                )
                                .padding(start = 18.dp, top = 18.dp, end = 34.dp, bottom = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.gallery),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier,
                                text = "Gallery",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    fontSize = 14.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        Row(
                            modifier = Modifier
                                .clickable(onClick = onCameraClick)
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainer,
                                    RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                                )
                                .padding(start = 34.dp, top = 18.dp, end = 18.dp, bottom = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.cam),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier,
                                text = "Camera",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onCancel()
                }
            ) {
                Text("Logout")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        },
        modifier = Modifier.background(Color.Blue) // Change background color of the dialog
    )
}
