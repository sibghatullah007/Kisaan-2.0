package com.final_year_project.kisaan10.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.R

//@Preview
@Composable
fun SplashScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.onBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(500.dp)
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .fillMaxWidth()
                            .clip(RectangleShape)
                    )
                }
            }
            Row (
                modifier = Modifier.fillMaxSize()
            ){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically)
                ){

                    Text(modifier = Modifier
                        .padding(top = 24.dp),
                        text = "Kisaan App",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                            fontSize = 30.sp,
                            letterSpacing = 1.sp,
                            color = Color(0xFF4CAF50)
                        )
                    )
                    Text(modifier = Modifier
                        .padding(top = 2.dp),
                        text = "Diagnose & Care for Plants Worldwide",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.ExtraLight)),
                            fontSize = 16.sp,
                            letterSpacing = 1.sp,
                            color = Color.Black
                        )
                    )
                }
            }
        }

    }
}
