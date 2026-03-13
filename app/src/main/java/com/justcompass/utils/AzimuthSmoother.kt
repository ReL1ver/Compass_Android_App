package com.justcompass.utils

class AzimuthSmoother {

    private var currentAzimuth = 0f
    private val smoothingFactor = 0.1f

    fun smooth(newAzimuth: Float): Float {

        var delta = newAzimuth - currentAzimuth

        if (delta > 180) delta -= 360
        if (delta < -180) delta += 360

        currentAzimuth += smoothingFactor * delta

        if (currentAzimuth < 0) currentAzimuth += 360f
        if (currentAzimuth > 360) currentAzimuth -= 360f

        return currentAzimuth
    }
}