package com.justcompass.utils

object LowPassFilter {

    private const val ALPHA = 0.15f

    fun filter(input: FloatArray, output: FloatArray): FloatArray {

        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }

        return output
    }
}