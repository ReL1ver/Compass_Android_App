package com.justcompass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.justcompass.ui.CompassScreen
import com.justcompass.ui.CompassViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CompassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompassScreen(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startSensors(this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopSensors()
    }
}