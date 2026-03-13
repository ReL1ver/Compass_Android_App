package com.justcompass.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcompass.sensors.CompassAccuracy
import com.justcompass.sensors.CompassSensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompassViewModel : ViewModel() {

    private val _azimuth = MutableStateFlow(0f)
    val azimuth: StateFlow<Float> = _azimuth

    private val _accuracy = MutableStateFlow(CompassAccuracy.LOW)
    val accuracy: StateFlow<CompassAccuracy> = _accuracy

    private var sensorManager: CompassSensorManager? = null

    fun startSensors(context: Context) {
        if (sensorManager != null) return

        sensorManager = CompassSensorManager(
            context,
            { azimuthValue ->
                viewModelScope.launch { _azimuth.value = azimuthValue }
            },
            { accuracyValue ->
                viewModelScope.launch { _accuracy.value = accuracyValue }
            }
        )
        sensorManager?.register()
    }

    fun stopSensors() {
        sensorManager?.unregister()
        sensorManager = null
    }
}