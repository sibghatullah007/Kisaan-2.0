package com.final_year_project.kisaan10.screens



import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
    fun IndeterminateCircularIndicator(loading:Boolean) {


        if (!loading) return

        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.White,
        )
    }


@Composable
fun LoadingAnimation1(
    circleColor: Color = MaterialTheme.colorScheme.primary,
    animationDelay: Int = 1000
) {
    var circleScale by remember { mutableStateOf(0f) }

    val circleScaleAnimate = animateFloatAsState(
        targetValue = circleScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDelay
            )
        )
    )

    LaunchedEffect(Unit) {
        circleScale = 1f
    }

    Box(
        modifier = Modifier
            .size(64.dp)
            .scale(circleScaleAnimate.value)
            .border(
                width = 4.dp,
                color = circleColor.copy(alpha = 1 - circleScaleAnimate.value),
                shape = CircleShape
            )
    )
}

@Preview
@Composable
fun prev(){
        val loading by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(3000)
    }
    IndeterminateCircularIndicator(!loading)
    LoadingAnimation1()
}