package com.final_year_project.kisaan10.screens

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.final_year_project.kisaan10.screens.components.CustomAlertDialog
import com.final_year_project.kisaan10.screens.components.ShimmerEffect
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream

@Composable
fun HomeScreen(
    onOkClick: () -> Unit,
    imageSelectionViewModel: ImageSelectionViewModel,
    recentDiseaseViewModel: RecentDiseaseViewModel,
    navController: NavHostController
) {
    val gradient = Brush.radialGradient(
        colors = listOf(Color.Green, Color.Black),
        radius = 370.0f,
        tileMode = TileMode.Clamp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        //        navTextHeading(text = "Diagnose")
        Text(
            text = "DIAGNOSE, \nTREAT, THRIVE",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_black_italic)),
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary
            ),
        )
        navTextDescription(text = "Identify and Cure Plant Disease")
        DiagnoseButton(gradient, imageSelectionViewModel, onOkClick)
        RecentDiseasesSection(recentDiseaseViewModel, navController)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DiagnoseButton(
    gradient: Brush,
    viewModel: ImageSelectionViewModel,
    onOkClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri
        }
    )

    val cameraUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                imageUri = cameraUri.value
            }
        }
    )

    fun launchGallery() {
        if (permissionState.allPermissionsGranted) {
            galleryLauncher.launch("image/*")
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    fun launchCamera() {
        if (permissionState.allPermissionsGranted) {
            val uri = createImageUri(context)
            cameraUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (showDialog) {
        CustomAlertDialog(
            onDismissRequest = { showDialog = false },
            onGalleryClick = {
                launchGallery()
                showDialog = false
            },
            onCameraClick = {
                launchCamera()
                showDialog = false
            },
            onCloseClick = { showDialog = false }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .sizeIn(minHeight = 200.dp, maxHeight = 500.dp)
                .padding(40.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val dashWidth = 10.dp.toPx()
                val dashGap = 8.dp.toPx()
                val strokeWidth = 1.dp.toPx()
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)

                drawRoundRect(
                    color = Color(0xFF4CAF50),
                    size = size,
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    style = Stroke(width = strokeWidth, pathEffect = pathEffect)
                )
            }
            if (imageUri == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.CameraAlt,
                        contentDescription = "Camera Icon",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Start Diagnosis",
                        style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.montserrat_bold)), color = MaterialTheme.colorScheme.onBackground)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Identify the crop diseases",
                        style = TextStyle(fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.montserrat)), color = MaterialTheme.colorScheme.onBackground)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Take A Photo",
                            style = TextStyle(fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.montserrat_medium)), color = Color.White)
                        )
                    }
                }
            } else {
                Image(
                    painter = rememberImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(7.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
    if (imageUri != null) {
        Row(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onOkClick()
                    viewModel.setSelectedImageUri(uri = imageUri)
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        brush = SolidColor(Color.White),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done Icon",
                    tint = Color.White,
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            IconButton(
                onClick = { imageUri = null },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        brush = SolidColor(Color.White),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
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

private fun createImageUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
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
fun RecentDiseasesSection(recentDiseaseViewModel: RecentDiseaseViewModel,navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
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
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Divider(Modifier.padding(top = 20.dp, bottom = 20.dp), color = MaterialTheme.colorScheme.background)
            val listOfRecentDisease by recentDiseaseViewModel.allDiseases.observeAsState(initial = emptyList())

             DiseaseRow(diseases = listOfRecentDisease, navController)

        }
    }
}

//data class Disease(val name: String, val image: Int)
@Composable
fun DiseaseRow(diseases: List<RecentDisease>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
    ) {
        items(diseases) { disease ->
            recentDisease(id = disease.id!!, name = disease.name, image = disease.pictureResId, navController)
        }
    }
}

@Composable
fun recentDisease(id: Long, name: String, image: String?,navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clickable { navController.navigate("recent_disease_result/${id}") },
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
                    color = MaterialTheme.colorScheme.onSurface
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentDiseaseResult(
    navController: NavHostController,
    recentDiseaseViewModel: RecentDiseaseViewModel,
    diseaseId:String?
) {
    val diseases by recentDiseaseViewModel.allDiseases.observeAsState(initial = emptyList())
    val specificDisease = diseaseId?.toLongOrNull()?.let { id ->
        diseases.find { it.id == id }
    }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Disease Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true },
                        modifier = Modifier.padding(end = 16.dp)) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            specificDisease?.let { disease ->
                DiseaseCard(disease)
            } ?: run {
                Text(text = "No disease detected")
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Delete Confirmation",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ))
            },
            containerColor = MaterialTheme.colorScheme.surface,
            text = {
                Text(text = "Do you want to delete this from history?",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Implement The Recent Disease Delete Screen Logic Here
                        if (specificDisease != null) {
                            recentDiseaseViewModel.delete(specificDisease)
                            navController.popBackStack()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Delete",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error
                        ))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ))
                }
            }
        )
    }
}
@Composable
fun ConfirmScreen(
    recentDiseaseViewModel: RecentDiseaseViewModel,
    imageSelectionViewModel: ImageSelectionViewModel,
    blogsViewModel: BlogsViewModel,
    wheatViewModel: WheatViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val imageUri by imageSelectionViewModel.selectedImageUri.observeAsState()
    val imageBitmap by remember { mutableStateOf(imageUri?.let { imageSelectionViewModel.uriToBitmap(context) }) }
    val isWheat by wheatViewModel.wheatDetectionResult.observeAsState()
    val diseasePredictionResult by wheatViewModel.diseasePredictionResult.observeAsState()
    val blogs by blogsViewModel.allBlogs.observeAsState(initial = emptyList())
    val specificBlog = diseasePredictionResult?.diseaseName?.let { diseaseName ->
        blogs.find { it.name == diseaseName }
    }

    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(imageBitmap) {
        imageBitmap?.let { wheatViewModel.detectWheat(it) }
    }

    LaunchedEffect(isWheat) {
        if (isWheat?.isWheat == true) {
            imageBitmap?.let { wheatViewModel.predictDisease(it) }
        }
    }

    LaunchedEffect(imageUri, isWheat, diseasePredictionResult) {
        if (imageUri != null && isWheat != null && (isWheat?.isWheat == false || (isWheat?.isWheat == true && diseasePredictionResult != null))) {
            delay(1500) // Introduce a 1.5 second delay before showing the content
            showContent = true
        }
    }

    if (!showContent) {
        ShimmerEffect()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = rememberImagePainter(data = imageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(580.dp),
                    contentScale = ContentScale.Fit
                )
                if (isWheat?.isWheat == true) {
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
                            val disease = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) { // Android 10 is Q
                                specificBlog?.let {
                                    imageUri?.let { it1 ->
                                        RecentDisease(
                                            name = diseasePredictionResult?.diseaseName ?: "",
                                            pictureResId = it1.toString(),
                                            symptom = it.symptom,
                                            treatment = specificBlog.treatment,
                                            prevention = specificBlog.prevention
                                        )
                                    }
                                }
                            } else {
                                specificBlog?.let {
                                    imageSelectionViewModel.getRealPathFromURI(context, imageUri)?.let { it1 ->
                                        RecentDisease(
                                            name = diseasePredictionResult?.diseaseName ?: "",
                                            pictureResId = it1,
                                            symptom = it.symptom,
                                            treatment = specificBlog.treatment,
                                            prevention = specificBlog.prevention
                                        )
                                    }
                                }
                            }

                            if (disease != null) {
                                recentDiseaseViewModel.insert(disease)
                            }
                            navController.navigate("diseased_result_route") { popUpTo("home") { inclusive = false } }
                        }
                    ) {
                        Text(
                            text = "View Results",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        )
                    }
                } else {
                    Text(
                        text = "Please select a valid image of wheat crop",
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
                    onClick = { navController.popBackStack() }
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Medium)),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
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
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
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
                color = MaterialTheme.colorScheme.onBackground,
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






@Composable
fun DiseaseCard(recentDisease: RecentDisease) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            val image = recentDisease.pictureResId
            if (image != null) {
                val uri = image.toUri()
                Log.v("Image Path", image)
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            else{
                Text(text = "Image not found")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = recentDisease.name,
                style = TextStyle(
                    fontFamily = FontFamily(
                        Font(R.font.roboto_bold, FontWeight.Bold),
                        Font(R.font.roboto_regular)
                    ),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            Divider()
            Spacer(modifier = Modifier.height(15.dp))
            if (recentDisease.name == "Healthy Wheat") {
                DiseaseContent(recentDisease = recentDisease, healthMessage = "Your crops look quite healthy", messageColor = MaterialTheme.colorScheme.primary)
            } else {
                DiseaseContent(recentDisease = recentDisease, healthMessage = "Check if your crop has following symptoms", messageColor = MaterialTheme.colorScheme.error)
            }
        }
    }
}
@Composable
fun DiseaseContent(recentDisease: RecentDisease, healthMessage: String, messageColor: Color) {
    Column {
        Text(
            text = healthMessage,
            style = TextStyle(
                fontFamily = FontFamily(
                    Font(R.font.roboto_medium, FontWeight.Medium),
                    Font(R.font.roboto_regular)
                ),
                fontSize = 14.sp,
                color = messageColor,
                fontStyle = FontStyle.Italic
            )
        )

        Spacer(modifier = Modifier.height(15.dp))
        BlogSection(title = "Symptoms", content = recentDisease.symptom)
        BlogSection(title = if (recentDisease.name == "Healthy Wheat") "Maintenance" else "Treatment", content = recentDisease.treatment)
        BlogSection(title = if (recentDisease.name == "Healthy Wheat") "Some Tips for Your Crop" else "Preventions", content = recentDisease.prevention)
    }
}






















//@Preview
//@Composable
//fun Preview(){
//    Kisaan10Theme{
//        HomeScreen()
//    }
//}