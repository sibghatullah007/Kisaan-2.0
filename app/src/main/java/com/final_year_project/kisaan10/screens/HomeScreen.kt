package com.final_year_project.kisaan10.screens

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.ViewModel.ImageSelectionViewModel
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.final_year_project.kisaan10.screens.components.navTextHeading
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.ByteArrayOutputStream

@Composable
fun HomeScreen(
    onOkClick: ()->Unit,
    viewModel: ImageSelectionViewModel
) {
    val gradient = Brush.radialGradient(
        colors = listOf(Color.Green, Color.Black),
        radius = 370.0f,
        tileMode = TileMode.Clamp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground)
    ) {
        navTextHeading(text = "Diagnose")
        navTextDescription(text = "Identify and Cure Plant Disease")
        DiagnoseButton(gradient,viewModel,onOkClick)
        RecentDiseasesSection()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DiagnoseButton(gradient: Brush,
                   viewModel: ImageSelectionViewModel,
                   onOkClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap: Bitmap? ->
            if (bitmap != null) {
                val uri = saveBitmapToUri(context, bitmap)
                imageUri = uri
            }
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .background(gradient, CircleShape)
                .size(200.dp),
            shape = CircleShape
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Icon(
                    Icons.Filled.AddAPhoto,
                    contentDescription = "Camera Icon",
                    tint = Color.White,
                    modifier = Modifier.size(55.dp)
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Select Option") },
                text = {
                    Column {
                        TextButton(onClick = {
                            showDialog = false
                            galleryLauncher.launch("image/*")
                        }) {
                            Icon(
                                Icons.Filled.PhotoLibrary,
                                contentDescription = "Gallery Icon",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gallery")
                        }
                        TextButton(onClick = {
                            showDialog = false
                            cameraLauncher.launch()
                        }) {
                            Icon(
                                Icons.Filled.PhotoCamera,
                                contentDescription = "Camera Icon",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Camera")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                containerColor = Color.White
            )
        }
    }
    if (imageUri != null) {
        Row(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onOkClick()
                viewModel.setSelectedImageUri(uri = imageUri)
            },modifier = Modifier
                .width(50.dp)
                .height(20.dp)
                .border(
                    width = 1.dp,
                    brush = SolidColor(Color.White),
                    shape = RoundedCornerShape(size = 20.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(size = 20.dp)
                )) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done Icon",
                    tint = Color.White,
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            IconButton(onClick = {imageUri = null}, Modifier
                .height(20.dp)
                .border(
                    width = 1.dp,
                    brush = SolidColor(Color.White),
                    shape = RoundedCornerShape(size = 20.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(size = 20.dp)
                )) {
                Icon(
                    Icons.Filled.Cancel,
                    contentDescription = "Cancel Icon",
                    tint = Color.White,
                )
            }
        }
    }
    Spacer(modifier = Modifier.size(5.dp))

    if (!permissionState.allPermissionsGranted) {
        LaunchedEffect(permissionState) {
            permissionState.launchMultiplePermissionRequest()
        }
    }
}

//private fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri {
//    val bytes = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//    val path = MediaStore.Images.Media.insertImage(
//        context.contentResolver,
//        bitmap,
//        "Title",
//        null
//    )
//    Log.v("justUri",path)
//    Log.v("parsed", Uri.parse(path).toString())
//    return Uri.parse(path)
//}
fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()

    // Using PNG for lossless compression
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

    // Using the MediaStore API for saving
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, "Title")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    }

    // Inserting image into the MediaStore
    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        context.contentResolver.openOutputStream(it).use { outputStream ->
            outputStream?.write(bytes.toByteArray())
        }
    }

    Log.v("justUri", uri.toString())
    Log.v("parsed", uri?.toString() ?: "null")

    return uri
}

@Composable
fun RecentDiseasesSection() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(8.dp)
            .fillMaxWidth()
            .height(310.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(40.dp))
                .fillMaxWidth()
                .height(310.dp)
                .border(0.5.dp, Color.LightGray, RoundedCornerShape(40.dp))
        ) {
            Text(
                text = "Recent Detected Plants",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )
            DiseaseRow()
            DiseaseRow(modifier = Modifier.padding(top = 15.dp))
        }
    }
}

@Composable
fun DiseaseRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        recentDisease(name = "Yellow Rust", image = R.drawable.crops)
        recentDisease(name = "Yellow Rust", image = R.drawable.crops)
        recentDisease(name = "Yellow Rust", image = R.drawable.crops)
    }
}

@Composable
fun recentDisease(name: String, image: Int) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = name,
            modifier = Modifier.padding(top = 7.dp),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                fontSize = 14.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
fun ConfirmScreen(viewModel: ImageSelectionViewModel, navController: NavHostController) {
    val imageUri = viewModel.getSelectedImageUri()
    Log.v("urii", imageUri.toString())
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageUri!=null) {
                Image(
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    contentScale = ContentScale.FillBounds
                )
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
                    onClick = {/* Todo */}) {
                    Text(
                        text ="View Results",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
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
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    onClick = {navController.popBackStack()}) {
                    Text(
                        text ="Cancel",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
        } else {
            Text("No image selected")
        }
    }
}























//package com.final_year_project.kisaan10.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AddAPhoto
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.TileMode
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.final_year_project.kisaan10.R
//import com.final_year_project.kisaan10.screens.components.navTextDescription
//import com.final_year_project.kisaan10.screens.components.navTextHeading
//import com.final_year_project.kisaan10.screens.components.recentDisease
//
//
//@Composable
//fun HomeScreen() {
//    val gradient = Brush.radialGradient(
//        0.0f to Color.Green,
//        1.0f to Color.Black,
//        radius = 370.0f,
//        tileMode = TileMode.Clamp
//    )
//
//    Column(
//        modifier = Modifier
////            .padding(innerPadding)
//            .fillMaxSize()
//            .background(color = MaterialTheme.colorScheme.onBackground),
////        verticalArrangement = Arrangement.Center,
////        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        navTextHeading(text = "Diagnose")
//        navTextDescription(text = "Identify and Cure Plant Disease")
//        Row(
//            modifier = Modifier
////                .padding(innerPadding)
//                .fillMaxWidth()
//                .height(230.dp),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedButton(
//                onClick = { /*TODO*/ },
//                Modifier
//                    .background(gradient, CircleShape)
//                    .height(200.dp)
//                    .width(200.dp),
//                shape = CircleShape
//
//                ) {
//                Icon(
//                    Icons.Filled.AddAPhoto,
//                    contentDescription = "Camera Icon",
//                    tint = Color.White,
//                    modifier = Modifier.size(55.dp)
//                    )
//            }
//        }
//        Row (
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.onBackground)
//                .padding(10.dp)
//                .fillMaxWidth()
//                .height(310.dp),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            Column(
//                modifier = Modifier
//                .background(Color.White, RoundedCornerShape(40.dp))
//                .fillMaxWidth()
//                .height(310.dp)
//                .border(0.5.dp, Color.LightGray, RoundedCornerShape(40.dp)),
//            ){
//                Row (modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    Text(text = "Recent Detected Plants",
//                        modifier = Modifier
//                            .padding(top = 10.dp),
//                        style = androidx.compose.ui.text.TextStyle(
//                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
//                            fontSize = 18.sp,
//                            color = Color.Black
//                        ),
//                    )
//                }
//
//                Row (
//                    Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
////                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                }
//                Row (
//                    Modifier.fillMaxWidth().padding(top = 15.dp),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
////                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                }
//            }
//
//        }
//    }
//}
////
//@Preview
//@Composable
//fun Preview(){
//    Kisaan10Theme{
//        HomeScreen()
//    }
//}