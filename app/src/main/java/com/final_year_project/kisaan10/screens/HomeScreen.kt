package com.final_year_project.kisaan10.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.final_year_project.kisaan10.R
import com.final_year_project.kisaan10.screens.components.navTextDescription
import com.final_year_project.kisaan10.screens.components.navTextHeading
import com.final_year_project.kisaan10.screens.components.recentDisease


@Composable
fun HomeScreen() {
    val gradient = Brush.radialGradient(
        0.0f to Color.Green,
        1.0f to Color.Black,
        radius = 370.0f,
        tileMode = TileMode.Clamp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground),
    ) {
        navTextHeading(text = "Diagnose")
        navTextDescription(text = "Identify and Cure Plant Disease")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                Modifier
                    .background(gradient, CircleShape)
                    .height(200.dp)
                    .width(200.dp),
                shape = CircleShape

                ) {
                Icon(
                    Icons.Filled.AddAPhoto,
                    contentDescription = "Camera Icon",
                    tint = Color.White,
                    modifier = Modifier.size(55.dp)
                    )
            }
        }
        Row (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(10.dp)
                .fillMaxWidth()
                .height(310.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                .background(Color.White, RoundedCornerShape(40.dp))
                .fillMaxWidth()
                .height(310.dp)
                .border(0.5.dp, Color.LightGray, RoundedCornerShape(40.dp)),
            ){
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Recent Detected Plants",
                        modifier = Modifier
                            .padding(top = 10.dp),
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                            fontSize = 18.sp,
                            color = Color.Black
                        ),
                    )
                }

                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
                }
                Row (
                    Modifier.fillMaxWidth().padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
//                    recentDisease(name = "Yellow Rust", image = R.drawable.crops)
                }
            }

        }
    }
}

//@Preview
//@Composable
//fun Preview(){
//    Kisaan10Theme{
//        HomeScreen()
//    }
//}