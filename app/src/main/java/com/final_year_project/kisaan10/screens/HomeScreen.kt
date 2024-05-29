package com.final_year_project.kisaan10.screens

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.ViewModel.BlogsViewModel
import com.final_year_project.kisaan10.ViewModel.ImageSelectionViewModel
import com.final_year_project.kisaan10.ViewModel.RecentDiseaseViewModel
import com.final_year_project.kisaan10.ViewModel.WheatViewModel
import com.final_year_project.kisaan10.localDB.Blogs
import com.final_year_project.kisaan10.localDB.RecentDisease
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.ByteArrayOutputStream

@Composable
fun HomeScreen(
    onOkClick: ()->Unit,
    imageSelectionViewModel: ImageSelectionViewModel,
    recentDiseaseViewModel: RecentDiseaseViewModel
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
//        navTextHeading(text = "Diagnose")
        Text(text = "DIAGNOSE, \nTREAT, THRIVE",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_black_italic)),
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(start = 25.dp, top = 10.dp)
        )
        navTextDescription(text = "Identify and Cure Plant Disease")
        DiagnoseButton(gradient,imageSelectionViewModel,onOkClick)
        RecentDiseasesSection(recentDiseaseViewModel)
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

//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(230.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        OutlinedButton(
//            onClick = { showDialog = true },
//            modifier = Modifier
//                .background(gradient, CircleShape)
//                .size(200.dp),
//            shape = CircleShape,
//            contentPadding = PaddingValues(0.dp) // Remove content padding
//        ) {
//            if (imageUri != null) {
//                Image(
//                    painter = rememberImagePainter(data = imageUri),
//                    contentDescription = "Selected Image",
//                    modifier = Modifier
//                        .size(200.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop // Fill the button with the image
//                )
//            } else {
//                Icon(
//                    Icons.Filled.AddAPhoto,
//                    contentDescription = "Camera Icon",
//                    tint = Color.White,
//                    modifier = Modifier.size(55.dp)
//                )
//            }
//        }
//
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                title = { Text(text = "Select Option") },
//                text = {
//                    Column {
//                        TextButton(modifier = Modifier.fillMaxWidth(),
//                            onClick = {
//                            showDialog = false
//                            galleryLauncher.launch("image/*")
//                        }) {
//                            Icon(
//                                Icons.Filled.PhotoLibrary,
//                                contentDescription = "Gallery Icon",
//                                tint = Color.Black,
//                                modifier = Modifier.size(24.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text("Gallery")
//                        }
//                        TextButton( modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//                            showDialog = false
//                            cameraLauncher.launch()
//                        }) {
//                            Icon(
//                                Icons.Filled.PhotoCamera,
//                                contentDescription = "Camera Icon",
//                                tint = Color.Black,
//                                modifier = Modifier.size(24.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text("Camera")
//                        }
//                    }
//                },
//                confirmButton = {
//                    TextButton(onClick = { showDialog = false }) {
//                        Text("Cancel")
//                    }
//                },
//                containerColor = Color.White
//            )
//        }
//    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.CameraAlt, // Replace with your camera icon resource
                contentDescription = "Camera Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Start Diagnosis",
                style = TextStyle(fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    color = Color.Black)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Identify the crop diseases",
                style = TextStyle(fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    color = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Take A Photo",
                    style = TextStyle(fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        color = Color.White)
                )
            }
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
fun RecentDiseasesSection(recentDiseaseViewModel: RecentDiseaseViewModel) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = "Recent Detected Plants",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )

            Divider(Modifier.padding(top = 20.dp, bottom = 20.dp), color = MaterialTheme.colorScheme.onBackground)
            val listOfRecentDisease by recentDiseaseViewModel.allDiseases.observeAsState(initial = emptyList())

             DiseaseRow(diseases = listOfRecentDisease)

        }
    }
}

data class Disease(val name: String, val image: Int)
@Composable
fun DiseaseRow(diseases: List<RecentDisease>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
    ) {
        items(diseases) { disease ->
            recentDisease(name = disease.name, image = disease.pictureResId)
        }
    }
}

@Composable
fun recentDisease(name: String, image: String?) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (image != null){
            val uri = image.toUri()
            Log.v("Image Path",image)
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 142.dp, height = 110.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                modifier = Modifier.padding(top = 7.dp),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
        }
        else{
            Image(
                painter = painterResource(id = R.drawable.recentimg),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                modifier = Modifier.padding(top = 7.dp),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ConfirmScreen(recentDiseaseViewModel: RecentDiseaseViewModel,imageSelectionViewModel: ImageSelectionViewModel, blogsViewModel: BlogsViewModel, wheatViewModel: WheatViewModel, navController: NavHostController) {
    val imageUri = imageSelectionViewModel.getSelectedImageUri()
    val imageBitmap = imageSelectionViewModel.uriToBitmap(LocalContext.current)
    val imageRealPath = imageSelectionViewModel.getRealPathFromURI(LocalContext.current,imageUri)
    imageBitmap?.let { wheatViewModel.detectWheat(it) }
    val isWheat = wheatViewModel.wheatDetectionResult.value?.isWheat

    if (imageBitmap != null) {
        wheatViewModel.predictDisease(imageBitmap)
    }
    val diseaseName = wheatViewModel.diseasePredictionResult.value?.diseaseName
    val diseaseConfidence = wheatViewModel.diseasePredictionResult.value?.confidence
    val blogs by blogsViewModel.allBlogs.observeAsState(initial = emptyList())
    val specificBlog = blogs.find { it.name == diseaseName }
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

                        val disease = specificBlog?.let {
                            imageRealPath?.let { it1 ->
                                RecentDisease(
                                    name = diseaseName!!,
                                    pictureResId = it1,
                                    symptom = it.symptom,
                                    treatment = specificBlog.treatment,
                                    prevention = specificBlog.prevention
                                )
                            }
                        }
                        if (disease != null) {
                            recentDiseaseViewModel.insert(disease)
                        }
                        navController.navigate("diseased_result_route"){ popUpTo("home") { inclusive = false } }
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




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiseasedResultScreen(
    recentDiseaseViewModel: RecentDiseaseViewModel,
    blogsViewModel: BlogsViewModel,
    selectionViewModel: ImageSelectionViewModel,
    wheatViewModel: WheatViewModel,
    navController: NavHostController
) {
    val imageUri = selectionViewModel.selectedImageUri.value
    val bitmapofImage = selectionViewModel.uriToBitmap(LocalContext.current)
    bitmapofImage?.let { wheatViewModel.predictDisease(it) }
    val diseaseName = wheatViewModel.diseasePredictionResult.value?.diseaseName
    val diseaseConfidence = wheatViewModel.diseasePredictionResult.value?.confidence
    val blogs by blogsViewModel.allBlogs.observeAsState(initial = emptyList())
    val specificBlog = blogs.find { it.name == diseaseName }
//    if (diseaseName != "Healthy Wheat") {
//        AppNotificationManager.sendNotification(
//            context = LocalContext.current,
//            "Disease Detected: $diseaseName",
//            "${diseaseName} is being detected in your crop.\n Make sure its treatment"
//        )
//    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Disease Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            specificBlog?.let { blog ->
                BlogCard(blog = blog)
            } ?: run {
                Text(text = "No disease detected")
            }
        }
    }
}

@Composable
fun BlogCard(blog: Blogs) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val context = LocalContext.current
            val resourceName = blog.pictureResId.removePrefix("@drawable/")
            val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

            Image(
                painter = painterResource(id = resourceId),
                contentDescription = blog.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = blog.name,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            Divider()
            Spacer(modifier = Modifier.height(15.dp))
            if (blog.name == "Healthy Wheat") {
                BlogContent(blog = blog, healthMessage = "Your crops look quite healthy", messageColor = MaterialTheme.colorScheme.primary)
            } else {
                BlogContent(blog = blog, healthMessage = "Check if your crop has following symptoms", messageColor = MaterialTheme.colorScheme.error )
            }
        }
    }
}

@Composable
fun BlogContent(blog: Blogs, healthMessage: String,messageColor:Color) {
    Column {
        Text(
            text = healthMessage,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                fontSize = 14.sp,
                color = messageColor,
                fontStyle = FontStyle.Italic
            )
        )

        Spacer(modifier = Modifier.height(15.dp))
        BlogSection(title = "Symptoms", content = blog.symptom)
        BlogSection(title = if (blog.name == "Healthy Wheat") "Maintenance" else "Treatment", content = blog.treatment)
        BlogSection(title = if (blog.name == "Healthy Wheat") "Some Tips for Your Crop" else "Preventions", content = blog.prevention)
    }
}

@Composable
fun BlogSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = content,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                fontSize = 15.sp,
                color = Color.Black,
                lineHeight = 25.sp,
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Divider()
        Spacer(modifier = Modifier.height(20.dp))
    }
}





























//@Preview
//@Composable
//fun Preview(){
//    Kisaan10Theme{
//        HomeScreen()
//    }
//}