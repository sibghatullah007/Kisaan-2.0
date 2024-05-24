package com.final_year_project.kisaan10.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.ViewModel.BlogsViewModel
import com.final_year_project.kisaan10.localDB.Blogs
import com.final_year_project.kisaan10.localDB.KissanDatabase
import com.final_year_project.kisaan10.screens.components.BlogItem
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.final_year_project.kisaan10.screens.components.navTextHeading

@Composable
fun BlogScreen(navController: NavHostController,viewModel: BlogsViewModel) {
    val blogs by viewModel.allBlogs.observeAsState(initial = emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground)
    ) {
        navTextHeading(text = "Blogs")
        navTextDescription(text = "Check Crop Disease Insights")

    if (blogs.isNotEmpty()) {
        BlogsGrid(blogs = blogs, navController = navController)
    } else {
        // Optionally, show a placeholder or loading indicator
        Text(text = "No blogs available")
    }
    }
}

@Composable
fun BlogsGrid(blogs: List<Blogs>, navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 3 columns
        modifier = modifier.padding(12.dp)
    ) {
        items(blogs) { blog ->
            // Remove the "@drawable/" prefix
            val resourceName = blog.pictureResId.removePrefix("@drawable/")
            // Get the resource ID dynamically
            val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(color = Color.White, RoundedCornerShape(10.dp))
                    .clickable { navController.navigate("blog_result_route/${blog.id}") },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit,
                )
                Text(
                    text = blog.name,
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .width(110.dp),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogResult(
    viewModel: BlogsViewModel,
    blogId: String?,
    navController: NavHostController // Callback for back button click
) {
    val blogs by viewModel.allBlogs.observeAsState(initial = emptyList())
    val specificBlog = blogs.find { it.id.toString() == blogId }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Blog Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back" )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        if (specificBlog != null) {
            Column(modifier = modifier) {
                BlogItem(specificBlog)
            }
        } else {
            Text(text = "No Blog Found", modifier = modifier)
        }
    }
}

