package com.final_year_project.kisaan10.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.final_year_project.kisaan10.components.navTextDescription
import com.final_year_project.kisaan10.components.navTextHeading


@Composable
fun NotificationScreen() {
    Column(
        modifier = Modifier
//            .padding(innerPadding)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        navTextHeading(text = "notification")
        navTextDescription(text = "Stay informed with Kisaan App")
    }
}