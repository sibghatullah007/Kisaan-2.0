package com.final_year_project.kisaan10.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.auth.googleAuth.UserData
import com.final_year_project.kisaan10.screens.components.screenTextField
import com.final_year_project.kisaan10.ui.theme.Kisaan10Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SettingScreen(
    navController:NavController,
    context: Context = LocalContext.current,
    userData: UserData?,
    onSignOut: () -> Unit
) {
    var listPrepared by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            prepareOptionsData()
            listPrepared = true
        }
    }

    if (listPrepared) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { UserDetails(context, userData) }
            items(optionsList) { item -> OptionsItemStyle(navController,item, context, onSignOut) }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

}

@Composable
private fun UserDetails(context: Context, userData: UserData?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color.Black),
            imageVector = Icons.Filled.Person,
            contentDescription = "Your Image",
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 16.dp)
            ) {
                if (userData?.username != null) {
                    Text(
                        text = userData.username!!,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                if (userData?.userEmail != null) {
                    Text(
                        text = userData.userEmail!!,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                            color = Color.Black,
                            letterSpacing = 0.8.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    Toast.makeText(context, "Edit Button", Toast.LENGTH_SHORT).show()
                }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Details",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}

@Composable
private fun OptionsItemStyle(navController:NavController,item: OptionsData, context: Context, onSignOut: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (item.title == "Logout") {
                    onSignOut()
                } else if (item.title == "App Info") {
                    navController.navigate("appInfo")
                } else if (item.title === "Help Center") {
                    navController.navigate("help_center")
                } else if (item.title == "Tell Friends") {
                    shareLink(
                        context, "Tired of loosing crops to undetected diseases?\n" +
                                "Your crop doctor is here with latest technology to save your crops and boost your yeilds.\n" +
                                "Click the link below to download the Kisaan App & try it now \n\n https://www.kisaan-app.com"
                    )
                } else if (item.title == "Clear Cache") {
                    clearCache(context)
                } else if (item.title === "Suggestion") {
                    navController.navigate("suggestion")
                } else {
                    Toast
                        .makeText(context, item.title, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = item.icon,
            contentDescription = item.title,
            tint = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = item.title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subTitle,
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = 0.8.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                        color = Color.Black
                    )
                )
            }

            Icon(
                modifier = Modifier.weight(1f),
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = item.title,
                tint = Color.Black.copy(alpha = 0.70f)
            )
        }
    }
}

data class OptionsData(val icon: ImageVector, val title: String, val subTitle: String)

private val optionsList: ArrayList<OptionsData> = ArrayList()

private fun prepareOptionsData() {
    optionsList.clear()
    val options = listOf(
        OptionsData(Icons.Outlined.Person, "Account", "Manage your account"),
        OptionsData(Icons.Outlined.Memory, "Clear Cache", "Clear your cache memory"),
        OptionsData(Icons.Outlined.SettingsSuggest, "Suggestion", "Suggest to improve"),
        OptionsData(Icons.Outlined.Policy, "Privacy Policy", "Terms and conditions"),
        OptionsData(Icons.AutoMirrored.Outlined.Help, "Help Center", "FAQs and customer support"),
        OptionsData(Icons.Outlined.Share, "Tell Friends", "Share this application"),
        OptionsData(Icons.Outlined.Info, "App Info", "About the app"),
        OptionsData(Icons.AutoMirrored.Outlined.Logout, "Logout", "Logout your Account")
    )
    optionsList.addAll(options)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(navController:NavController) {
    Kisaan10Theme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Suggestion", color = MaterialTheme.colorScheme.primary)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back" )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        ) { paddingValues ->
            val inner = paddingValues
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paint(
                            painter = painterResource(id = R.drawable.background_image_app_info),
                            contentScale = ContentScale.FillHeight
                        )
                        .height(200.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Kisaan App Logo",
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.White)
                                .padding(vertical = 16.dp),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Kisaan App",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                                fontSize = 20.sp,
                                color = Color.White
                            ),
                        )
                        Text(
                            text = "Diagnose & Care for plants worldwide",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                                fontSize = 14.sp,
                                color = Color.White
                            ),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Tired of losing crops to undetected diseases? Your Crop Doctor is here with the latest technology to save your crops and boost your yields. Download Kisaan App & try it now!",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                            fontSize = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Justify,
                            lineHeight = 20.sp,
                            letterSpacing = 2.sp
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(Color.White, shape = RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gmail_logo),
                        contentDescription = "Gmail Icon",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Gmail.com",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                                fontSize = 16.sp,
                                color = Color.Red
                            ),
                        )
                        Text(
                            text = "kisaan002@gmail.com",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                                fontSize = 15.sp,
                                color = Color.Black
                            ),
                        )
                    }
                }
            }
        }
    }
}

fun shareLink(context: Context, url: String) {
    val sendIntent: Intent= Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

fun clearCache(context: Context) {
    try {
        val cacheSizeBefore = context.cacheDir.totalSpace
        context.cacheDir.deleteRecursively()
        context.externalCacheDir?.deleteRecursively()
        val cacheSizeAfter = context.cacheDir.totalSpace
        val cacheCleared = cacheSizeBefore - cacheSizeAfter
        val cacheClearedText = formatFileSize(cacheCleared)
        showToast(context, "Cleared $cacheClearedText of cache")
    } catch (e: Exception) {
        e.printStackTrace()
        showToast(context, "Failed to clear cache")
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun formatFileSize(size: Long): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var fileSize = size.toDouble()
    var unitIndex = 0
    while (fileSize > 1024 && unitIndex < units.size - 1) {
        fileSize /= 1024
        unitIndex++
    }
    return String.format("%.2f %s", fileSize, units[unitIndex])
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("QueryPermissionsNeeded")
@Composable
fun SuggestionScreen(navController: NavController) {
//    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
//    var emailError by remember { mutableStateOf(false) }
    val subjectError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Kisaan10Theme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "App Info", color = MaterialTheme.colorScheme.primary)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        ) { paddingValues ->
            val inner = paddingValues
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
                    .background(MaterialTheme.colorScheme.onBackground),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Red, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "Remember your suggestions are quite valuable to improve the application!",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                                fontSize = 14.sp,
                                color = Color.White,
                                textAlign = TextAlign.Justify,
                                lineHeight = 20.sp,
                                letterSpacing = 2.sp
                            ),
                        )
                    }
                }
//                OutlinedTextField(
//                    value = email,
//                    onValueChange = {
//                        email = it
//                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(
//                            start = textFieldPadding,
//                            end = textFieldPadding,
//                            top = textFieldPadding,
//                        )
//                        .background(Color.White, RoundedCornerShape(cornerRadius)),
//                    singleLine = true,
//                    shape = RoundedCornerShape(cornerRadius),
//                    textStyle = screenTextField(MaterialTheme.colorScheme.primary),
//                    placeholder = {
//                        Text(
//                            text = "Enter your Email",
//                            style = screenTextField(Color(0xFF808080))
//                        )
//                    },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Outlined.Mail,
//                            contentDescription = "mailIcon",
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                    },
//                    colors = OutlinedTextFieldDefaults.colors(
//                        cursorColor = MaterialTheme.colorScheme.primary,
//                        focusedBorderColor = MaterialTheme.colorScheme.primary,
//                        unfocusedBorderColor = Color.White,
//                    ),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Email,
//                        imeAction = ImeAction.Next
//                    )
//                )
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = textFieldPadding,
                            end = textFieldPadding,
                            top = textFieldPadding,
                        )
                        .background(Color.White, RoundedCornerShape(cornerRadius)),
                    singleLine = true,
                    shape = RoundedCornerShape(cornerRadius),
                    textStyle = screenTextField(MaterialTheme.colorScheme.primary),
                    placeholder = {
                        Text(
                            text = "Subject of Suggestion",
                            style = screenTextField(Color(0xFF808080))
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Mail,
                            contentDescription = "mailIcon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                if (subjectError) {
                    Text(text = "Subject cannot be empty", color = Color.Red, fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 20.dp,
                        )
                        .background(Color.White, RoundedCornerShape(cornerRadius)),
                    shape = RoundedCornerShape(cornerRadius),
                    textStyle = screenTextField(MaterialTheme.colorScheme.primary),
                    placeholder = {
                        Text(
                            text = "Enter your Suggestion here",
                            style = screenTextField(Color(0xFF808080))
                        )
                    },

                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .padding(top = 14.dp) // Adjust the top padding as needed
                                .fillMaxHeight()
                                .align(Alignment.Start),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SettingsSuggest,
                                contentDescription = "suggestionIcon",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ), onValueChange = { description = it }
                )
                if (descriptionError) {
                    Text(text = "Description cannot be empty", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

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
//                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        descriptionError = description.isBlank()
                        if (/*!emailError &&*/ !subjectError && !descriptionError) {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf("kisaan002@gmail.com"))
                                putExtra(Intent.EXTRA_SUBJECT, subject)
                                putExtra(Intent.EXTRA_TEXT, description)
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            }
                        }
                    },
                ) {
                    Text(text = "Send", color = Color.White)
                }
            }
        }
    }
}

































//
//
//@Preview
//@Composable
//fun Preview(){
// SuggestionScreen()
//}