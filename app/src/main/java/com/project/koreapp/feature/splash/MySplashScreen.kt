package com.project.koreapp.feature.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.koreapp.R
import com.project.koreapp.navigation.Routes

@Composable
fun MySplashScreen(
    navController: NavHostController = rememberNavController(),
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val startNextScreen: Boolean by splashViewModel.startNextScreen.observeAsState(false)
    val milliseconds: Long by splashViewModel.milliseconds.observeAsState(3500)

    if (startNextScreen) {
        navController.popBackStack()
        navController.navigate(Routes.MainScreen.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ) {
        SecondsTimer(milliseconds)

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            var circleVisibility by rememberSaveable { mutableStateOf(false) }
            circleVisibility = milliseconds in 500..3000

            var isFirstBorderColor by rememberSaveable { mutableStateOf(true) }
            val borderColor by animateColorAsState(
                targetValue = if (isFirstBorderColor) Color.Black else Color.White,
                label = "",
                animationSpec = tween(500)
            )
            isFirstBorderColor = milliseconds > 2000

            AnimatedVisibility(visible = circleVisibility) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(200.dp),
                    border = BorderStroke(3.dp, borderColor),
                    colors = CardDefaults.cardColors(containerColor = Color.Red)
                ) {

                }
            }

            var imageVisibility by rememberSaveable { mutableStateOf(false) }
            imageVisibility = milliseconds in 500..3000
            AnimatedVisibility(
                visible = imageVisibility,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}


@Composable
fun SecondsTimer(milliseconds: Long) {
    val seconds = (milliseconds / 1000).toInt()

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Card(
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.White),
            modifier = Modifier.size(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = seconds.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun MyPreview() {
    MySplashScreen()
}