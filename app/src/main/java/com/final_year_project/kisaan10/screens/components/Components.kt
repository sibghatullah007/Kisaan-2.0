package com.final_year_project.kisaan10.screens.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.localDB.Blogs
import com.final_year_project.kisaan10.screens.cornerRadius
import com.final_year_project.kisaan10.screens.textFieldPadding

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
            .background(Color.White, RoundedCornerShape(cornerRadius)),
        value = text,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.White,
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
        modifier = Modifier.padding(start = 15.dp, top = 5.dp)
    )
}

@Composable
fun navTextDescription(text: String){
    Text(
        text =text,
        style = androidx.compose.ui.text.TextStyle(
            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(start = 15.dp, top = 5.dp)
    )
}

@Composable
fun BlogItem(blog: Blogs) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val context = LocalContext.current

            // Remove the "@drawable/" prefix
            val resourceName = blog.pictureResId.removePrefix("@drawable/")

            // Get the resource ID dynamically
            val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

            Image(
                painter = painterResource(id = resourceId), // Ensure pictureResId is a valid resource ID
                contentDescription = blog.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = blog.name,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Symptom: ${blog.symptom}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Treatment: ${blog.treatment}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Prevention: ${blog.prevention}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

//@Composable
//fun recentDisease(name:String, image:Int){
//    Row(
//        Modifier.padding(
//            start = 10.dp,
//            end = 10.dp
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(top = 10.dp, start = 10.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painterResource(id = image),
//                contentDescription = null,
//                modifier = Modifier.size(80.dp)
//            )
//            Text(text = name,
//                modifier = Modifier
//                    .padding(top = 7.dp),
//                style = TextStyle(
//                    fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
//                    fontSize = 14.sp,
//                    color = Color.Black,
//                ),
//            )
//        }
//    }
//}
////
//@Composable
//fun settingsItem(name: String) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(
//            modifier = Modifier
//                .width(70.sp)
//        ){
//            Text(
//                text = name,
//                modifier = Modifier.padding(start = 15.dp, top = 10.dp),
//                style = TextStyle(
//                    fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
//                    fontSize = 14.sp,
//                    color = Color.Black,
//                )
//            )
//            Divider(
//                modifier = Modifier
//                    .padding(horizontal = 7.dp),
//                color = Color.White,
//                thickness = 1.dp
//            )
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//        ){
//
//        }
//
//        Icon(
//            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
//            contentDescription = "forwardArrow",
//            tint = Color.Black,
//            modifier = Modifier.size(15.dp)
//        )
//    }
//    Spacer(modifier = Modifier.height(10.dp))
//}
//
//
//@Composable
//fun arrowIcon(){
//    Icon(
//        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
//        contentDescription = "forwardArrow",
//        tint = Color.Black,
//        modifier = Modifier.size(15.dp)
//    )
//}
//@Composable
//@Preview
//fun prevv(){
//    settingsItem(name = "Abc")
//}
//
