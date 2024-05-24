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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.ViewModel.ImageSelectionViewModel
import com.final_year_project.kisaan10.ViewModel.WheatViewModel
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.final_year_project.kisaan10.screens.components.navTextHeading
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
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
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp) // Remove content padding
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop // Fill the button with the image
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
            val defaultImage = R.drawable.recentimg
            val defaultName = "No File"

            val recentDiseases = listOf(
                Disease(name = "Healthy Wheat", image = R.drawable.healthy_wheat_),
                Disease(name = "Yellow Rust", image = R.drawable.yellow_rust_wheat_),
                Disease(name = "Healthy Wheat", image = R.drawable.healthy_wheat_),
                Disease(name = "Yellow Rust", image = R.drawable.yellow_rust_wheat_),
                Disease(name = "Healthy Wheat", image = R.drawable.healthy_wheat_),
                Disease(name = "Yellow Rust", image = R.drawable.yellow_rust_wheat_),
                Disease(name = "Healthy Wheat", image = R.drawable.healthy_wheat_),
                Disease(name = "Yellow Rust", image = R.drawable.yellow_rust_wheat_),
                Disease(name = defaultName, image = defaultImage), // Add default data if no recent diseas
                Disease(name = defaultName, image = defaultImage) // Add default data if no recent disease

            )

//            DiseaseRow(diseases = recentDiseases)
            DiseaseRow(diseases = recentDiseases)

        }
    }
}

data class Disease(val name: String, val image: Int)
@Composable
fun DiseaseRow(diseases: List<Disease>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columns
        modifier = modifier.padding(12.dp)
    ) {
        items(diseases) { disease ->
            recentDisease(name = disease.name, image = disease.image)
        }
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
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Fit
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
fun ConfirmScreen(imagewheatViewModel: ImageSelectionViewModel, wheatViewModel: WheatViewModel, navController: NavHostController) {
    val imageUri = imagewheatViewModel.getSelectedImageUri()
    val imageBitmap = imagewheatViewModel.uriToBitmap(LocalContext.current)
    imageBitmap?.let { wheatViewModel.detectWheat(it) }
    val isWheat = wheatViewModel.wheatDetectionResult.value?.isWheat
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
                        .height(580.dp),
                    contentScale = ContentScale.Fit
                )
            if (isWheat == true) {
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
                        navController.navigate("diseased_result_route")
                    }) {
                    Text(
                        text = "View Results",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }
            else{
                Text(
                    text = "Please select the Valid Image of Wheat Crop",
                    modifier = Modifier.padding(top = 20.dp),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
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


@Composable
fun DiseasedResultScreen() {
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
            val resourceName = "brown_rust_wheat"

            // Get the resource ID dynamically
            val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

            Image(
                painter = painterResource(id = resourceId), // Ensure pictureResId is a valid resource ID
                contentDescription = "Disease Name",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Disease Name",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary),
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Check if your crop has following symptoms",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontStyle = FontStyle.Italic)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Symptoms",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "These are disease symptoms",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                    fontSize = 15.sp,
                    color = Color.Black,
                    lineHeight = 25.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Treatment",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "These are disease treatment",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                    fontSize = 15.sp,
                    color = Color.Black,
                    lineHeight = 25.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Preventions",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                    fontSize = 20.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text ="These are disease preventions",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                    fontSize = 15.sp,
                    color = Color.Black,
                    lineHeight = 25.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(15.dp))

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