package com.final_year_project.kisaan10.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.ViewModel.BlogsViewModel
import com.final_year_project.kisaan10.localDB.Blogs
import com.final_year_project.kisaan10.localDB.KissanDatabase
import com.final_year_project.kisaan10.screens.components.BlogItem

@Composable
fun BlogScreen(viewModel: BlogsViewModel) {
    val blogs by viewModel.allBlogs.observeAsState(initial = emptyList())
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(blogs) { blog ->
            BlogItem(blog)
        }
    }
}