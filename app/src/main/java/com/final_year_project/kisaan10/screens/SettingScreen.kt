package com.final_year_project.kisaan10.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.auth.googleAuth.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SettingScreen(
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
            items(optionsList) { item -> OptionsItemStyle(item, context, onSignOut) }
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
private fun OptionsItemStyle(item: OptionsData, context: Context, onSignOut: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (item.title == "Logout") {
                    onSignOut()
                } else {
                    Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
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

private fun prepareOptionsData() {
    optionsList.clear()
    val options = listOf(
        OptionsData(Icons.Outlined.Person, "Account", "Manage your account"),
        OptionsData(Icons.Outlined.Memory, "Clear Cache", "Clear your cache memory"),
        OptionsData(Icons.Outlined.SettingsSuggest, "Suggestion", "Suggest to improve"),
        OptionsData(Icons.Outlined.Policy, "Privacy Policy", "Terms and conditions"),
        OptionsData(Icons.Outlined.Help, "Help Center", "FAQs and customer support"),
        OptionsData(Icons.Outlined.Share, "Tell Friends", "Share this application"),
        OptionsData(Icons.Outlined.Info, "App Info", "About the app"),
        OptionsData(Icons.Outlined.Logout, "Logout", "Logout your Account")
    )
    optionsList.addAll(options)
}

data class OptionsData(val icon: ImageVector, val title: String, val subTitle: String)

private val optionsList: ArrayList<OptionsData> = ArrayList()









































//package com.final_year_project.kisaan10.screens
//
//import android.content.Context
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.outlined.ChevronRight
//import androidx.compose.material.icons.outlined.Edit
//import androidx.compose.material.icons.outlined.FavoriteBorder
//import androidx.compose.material.icons.outlined.Help
//import androidx.compose.material.icons.outlined.Info
//import androidx.compose.material.icons.outlined.Logout
//import androidx.compose.material.icons.outlined.Memory
//import androidx.compose.material.icons.outlined.Person
//import androidx.compose.material.icons.outlined.Policy
//import androidx.compose.material.icons.outlined.SettingsSuggest
//import androidx.compose.material.icons.outlined.Share
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.final_year_project.kisaan10.R
//import com.final_year_project.kisaan10.auth.googleAuth.UserData
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//
////@Composable
////fun SettingScreen(
////    userData: UserData?,
////    onSignOut:()->Unit
////) {
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .background(Color.LightGray)
////    ) {
////        Text(text = "Home Screen", fontSize = 20.sp)
////        if (userData?.username != null) {
////            Text(text = userData.username!!,
////                textAlign = TextAlign.Center,
////                fontSize = 36.sp,
////                fontWeight = FontWeight.SemiBold
////            )
////        }
////        Button(onClick = onSignOut) {
////            Text(text = "Sign out")
////        }
////    }
////}
//private val optionsList: ArrayList<OptionsData> = ArrayList()
//
////@Composable
////fun TopAppbarProfile(context: Context) {
////    TopAppBar(
////        title = {
////            Text(
////                text = "Profile",
////                maxLines = 1,
////                overflow = TextOverflow.Ellipsis
////            )
////        },
////        backgroundColor = MaterialTheme.colors.background,
////        elevation = 4.dp,
////        navigationIcon = {
////            IconButton(onClick = {
////                Toast.makeText(context, "Nav Button", Toast.LENGTH_SHORT).show()
////            }) {
////                Icon(
////                    Icons.Filled.ArrowBack,
////                    contentDescription = "Go back",
////                )
////            }
////        }
////    )
////}
//
//@Composable
//fun SettingScreen(
//    context: Context = LocalContext.current.applicationContext,
//    userData: UserData?,
//    onSignOut:()->Unit) {
//
//    // This indicates if the optionsList has data or not
//    // Initially, the list is empty. So, its value is false.
//    var listPrepared by remember {
//        mutableStateOf(false)
//    }
//
//    LaunchedEffect(Unit) {
//        withContext(Dispatchers.Default) {
//            optionsList.clear()
//
//            // Add the data to optionsList
//            prepareOptionsData()
//
//            listPrepared = true
//        }
//    }
//
//    if (listPrepared) {
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//
//            item {
//                // User's image, name, email and edit button
//                UserDetails(context = context,userData)
//            }
//
//            // Show the options
//            items(optionsList) { item ->
//                OptionsItemStyle(item = item, context = context,
//                    onSignOut = onSignOut)
//            }
//
//        }
//    }
//}
//
//// This composable displays user's image, name, email and edit button
//@Composable
//private fun UserDetails(context: Context,
//                        userData: UserData?) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        // User's image
//        Icon(
//            modifier = Modifier
//                .size(72.dp)
//                .clip(shape = CircleShape)
//                .background(Color.Black),
//            imageVector = Icons.Filled.Person,
//            contentDescription = "Your Image",
//        )
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment= Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(weight = 3f, fill = false)
//                    .padding(start = 16.dp)
//            ) {
//
//                // User's name
//                if (userData?.username != null) {
//            Text(text = userData.username!!,
//                textAlign = TextAlign.Center,
//                style = TextStyle(
//                fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
//                fontSize = 18.sp,
//                color = MaterialTheme.colorScheme.primary
//            )
//            )
//        }
//
//                Spacer(modifier = Modifier.height(2.dp))
//
//                // User's email
//                if (userData?.username != null){
//                    Text(
//                        text = userData.userEmail!!,
//                        style = TextStyle(
//                            fontSize = 14.sp,
//                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
//                            color = Color.Black,
//                            letterSpacing = (0.8).sp
//                        ),
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//
//            }
//
//            // Edit button
//            IconButton(
//                modifier = Modifier
//                    .weight(weight = 1f, fill = false),
//                onClick = {
//                    Toast.makeText(context, "Edit Button", Toast.LENGTH_SHORT).show()
//                }) {
//                Icon(
//                    modifier = Modifier.size(24.dp),
//                    imageVector = Icons.Outlined.Edit,
//                    contentDescription = "Edit Details",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
//
//        }
//    }
//}
//
//// Row style for options
//@Composable
//private fun OptionsItemStyle(item: OptionsData, context: Context ,
//                             onSignOut:()->Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(enabled = true) {
//                if (item.title == "Logout"){
//                        onSignOut()
//                }else{
//                    Toast
//                        .makeText(context, item.title, Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//            .padding(all = 16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        // Icon
//        Icon(
//            modifier = Modifier
//                .size(32.dp),
//            imageVector = item.icon,
//            contentDescription = item.title,
//            tint = MaterialTheme.colorScheme.primary
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(weight = 3f, fill = false)
//                    .padding(start = 16.dp)
//            ) {
//
//                // Title
//                Text(
//                    text = item.title,
//                    style = TextStyle(
//                        fontSize = 18.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
//                        color = MaterialTheme.colorScheme.primary
//
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(2.dp))
//
//                // Sub title
//                Text(
//                    text = item.subTitle,
//                    style = TextStyle(
//                        fontSize = 14.sp,
//                        letterSpacing = (0.8).sp,
//                        fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
//                        color = Color.Black
//                    )
//                )
//
//            }
//
//            // Right arrowicon
//            Icon(
//                modifier = Modifier
//                    .weight(weight = 1f, fill = false),
//                imageVector = Icons.Outlined.ChevronRight,
//                contentDescription = item.title,
//                tint = Color.Black.copy(alpha = 0.70f)
//            )
//        }
//
//    }
//}
//
//private fun prepareOptionsData() {
//
//    val appIcons = Icons.Outlined
//
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.Person,
//            title = "Account",
//            subTitle = "Manage your account"
//        )
//    )
//
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.Memory,
//            title = "Clear Cache",
//            subTitle = "Clear your cache memory"
//        )
//    )
//
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.SettingsSuggest,
//            title = "Suggestion",
//            subTitle = "Suggest to improve"
//        )
//    )
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.Policy,
//            title = "Privacy Policy",
//            subTitle = "Terms and conditions"
//        )
//    )
//
//
//
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.Help,
//            title = "Help Center",
//            subTitle = "FAQs and customer support"
//        )
//    )
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.Share,
//            title = "Tell Friends",
//            subTitle = "Share this application"
//        )
//    )
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.Info,
//            title = "App Info",
//            subTitle = "About the app"
//        )
//    )
//    optionsList.add(
//        OptionsData(
//            icon = appIcons.Logout,
//            title = "Logout",
//            subTitle = "Logout your Account"
//        )
//    )
//
//}
//
//data class OptionsData(val icon: ImageVector, val title: String, val subTitle: String)
////@Composable
////fun SettingScreen(
////    userData: UserData?,
////    onSignOut:()->Unit
////) {
////
////}
////
//@Preview
//@Composable
//fun Preview(){
// SettingScreen()
////    ProfileEcommerce("",,{})
//}