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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.ViewModel.BlogsViewModel
import com.final_year_project.kisaan10.localDB.Blogs
import com.final_year_project.kisaan10.screens.components.BlogItem
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.final_year_project.kisaan10.screens.components.navTextHeading

@Composable
fun BlogScreen(navController: NavHostController,viewModel: BlogsViewModel) {
    val blogs by viewModel.allBlogs.observeAsState(initial = emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
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
        columns = GridCells.Fixed(2), // 2 columns
        modifier = modifier.padding(top = 12.dp)
    ) {
        items(blogs) { blog ->
            // Remove the "@drawable/" prefix
            val resourceName = blog.pictureResId.removePrefix("@drawable/")
            // Get the resource ID dynamically
            val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp))
                    .clickable { navController.navigate("blog_result_route/${blog.id}") },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 150.dp, height = 120.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = blog.name,
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .width(110.dp),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
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
                title = { Text(text = "Blog Details", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back" )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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

