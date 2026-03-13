package com.justcompass.sensors

import android.content.Context
import android.hardware.*
import android.os.VibrationEffect
import android.os.Vibrator
import com.justcompass.utils.LowPassFilter
import kotlin.math.abs

class CompassSensorManager(
    context: Context,
    private val onAzimuthChanged: (Float) -> Unit,
    private val onAccuracyChanged: (CompassAccuracy) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val vibrator: Vibrator? = context.getSystemService(Vibrator::class.java)

    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)

    private var lastAzimuth = 0f
    private val smoothingFactor = 0.1f  // всегда 0.1, убрано как параметр функции

    fun register() {
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    fun unregister() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = LowPassFilter.filter(event.values, gravity)
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = LowPassFilter.filter(event.values, geomagnetic)
        }

        val rMatrix = FloatArray(9)
        val iMatrix = FloatArray(9)
        if (SensorManager.getRotationMatrix(rMatrix, iMatrix, gravity, geomagnetic)) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rMatrix, orientation)

            var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            if (azimuth < 0) azimuth += 360f


            val smoothAzimuth = lerpAngle(lastAzimuth, azimuth)
            val diff = abs(smoothAzimuth - lastAzimuth)

            if (diff > 2f) vibrateShort()
            if (smoothAzimuth in 358f..360f || smoothAzimuth in 0f..2f) vibrateLong()

            lastAzimuth = smoothAzimuth
            onAzimuthChanged(smoothAzimuth)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (sensor?.type != Sensor.TYPE_MAGNETIC_FIELD) return

        val acc = when (accuracy) {
            SensorManager.SENSOR_STATUS_UNRELIABLE -> CompassAccuracy.UNRELIABLE
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> CompassAccuracy.LOW
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> CompassAccuracy.MEDIUM
            else -> CompassAccuracy.HIGH
        }
        onAccuracyChanged(acc)
    }

    private fun lerpAngle(current: Float, target: Float): Float {
        val delta = (target - current + 540) % 360 - 180
        return (current + smoothingFactor * delta + 360) % 360
    }

    private fun vibrateShort() {
        vibrator?.takeIf { it.hasVibrator() }?.vibrate(
            VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
        )
    }

    private fun vibrateLong() {
        vibrator?.takeIf { it.hasVibrator() }?.vibrate(
            VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
}