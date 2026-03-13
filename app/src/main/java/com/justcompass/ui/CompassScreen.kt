package com.justcompass.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justcompass.sensors.CompassAccuracy
import kotlinx.coroutines.delay

@Composable
fun CompassScreen(viewModel: CompassViewModel) {

    val azimuth by viewModel.azimuth.collectAsState()
    val accuracy by viewModel.accuracy.collectAsState()

    var displayedAzimuth by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(azimuth) {
        while (true) {
            val delta = ((azimuth - displayedAzimuth + 540) % 360) - 180
            displayedAzimuth = (displayedAzimuth + delta * 0.1f + 360) % 360
            delay(16) // ~60 FPS
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CompassDial(displayedAzimuth, Modifier.size(320.dp))

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${azimuth.toInt()}°", fontSize = 36.sp)

            if (accuracy != CompassAccuracy.HIGH) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Move phone in ∞ shape to calibrate", fontSize = 16.sp)
            }
        }
    }
}