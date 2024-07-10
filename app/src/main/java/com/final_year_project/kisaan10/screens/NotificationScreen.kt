package com.final_year_project.kisaan10.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.final_year_project.kisaan10.screens.components.navTextHeading

@Composable
fun NotificationScreen(notifications: List<Notification>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        item {
            navTextHeading(text = "Notifications")
//            Spacer(modifier = Modifier.height(8.dp))
            navTextDescription(text = "Stay informed with Kisaan App")

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(notifications) { notification ->
            NotificationBox(
                heading = notification.heading,
                description = notification.description,
                time = notification.time
            )
        }
    }
}

@Composable
fun NotificationBox(
    heading: String,
    description: String,
    time: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 10.dp, vertical = 16.dp)
    ) {
        Column {
            Text(
                text = heading,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Normal)),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
        Text(
            text = time,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.TopEnd)
        )
    }
}


data class Notification(
    val heading: String,
    val description: String,
    val time: String
)
