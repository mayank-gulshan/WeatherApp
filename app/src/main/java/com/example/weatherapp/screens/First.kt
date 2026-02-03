package com.example.weatherapp.screens

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.Screens
import com.example.weatherapp.data.LocationData
import com.example.weatherapp.data.LocationUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.Permissions

@Composable
fun FirstScreenUI(
                  onNextClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.ic_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Weather App",
                color = Color.White,
                fontSize = 50.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 180.dp)
            )

            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 220.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "NEXT",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}
@Composable
fun FirstScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val locationUtils = remember { LocationUtils(context) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                navController.navigate(Screens.SecondScreen.route) {
                    popUpTo(Screens.FirstScreen.route) { inclusive = true }
                }
            }
        }

    FirstScreenUI(
        onNextClick = {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    )
}

