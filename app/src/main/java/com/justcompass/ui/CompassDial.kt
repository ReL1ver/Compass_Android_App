package com.justcompass.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompassDial(rotation: Float, modifier: Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2
        val center = center

        drawCircle(Color(0xFFEFEFEF), radius)
        for (i in 0 until 360 step 15) {
            val angle = Math.toRadians(i.toDouble())
            val startX = center.x + (radius - 20) * cos(angle).toFloat()
            val startY = center.y + (radius - 20) * sin(angle).toFloat()
            val endX = center.x + radius * cos(angle).toFloat()
            val endY = center.y + radius * sin(angle).toFloat()
            drawLine(Color.DarkGray, Offset(startX, startY), Offset(endX, endY), 6f)
        }

        rotate(rotation, pivot = center) {
            val arrowWidth = 12f
            val path = Path()
            path.moveTo(center.x, center.y - radius * 0.7f)
            path.lineTo(center.x - arrowWidth, center.y)
            path.lineTo(center.x + arrowWidth, center.y)
            path.close()
            drawPath(path, Color.Red)
        }

        drawCircle(Color.White, 12f, center)

        val paint = android.graphics.Paint().apply {
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 40f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
            typeface = android.graphics.Typeface.MONOSPACE
        }

        val offsetMain = 70f
        val offsetDiagonal = radius / 1.7f

        drawContext.canvas.nativeCanvas.drawText("С", center.x, center.y - radius + offsetMain, paint)
        drawContext.canvas.nativeCanvas.drawText("В", center.x + radius - offsetMain, center.y, paint)
        drawContext.canvas.nativeCanvas.drawText("Ю", center.x, center.y + radius - offsetMain, paint)
        drawContext.canvas.nativeCanvas.drawText("З", center.x - radius + offsetMain, center.y, paint)

        drawContext.canvas.nativeCanvas.drawText("СВ", center.x + offsetDiagonal, center.y - offsetDiagonal, paint)
        drawContext.canvas.nativeCanvas.drawText("ЮВ", center.x + offsetDiagonal, center.y + offsetDiagonal, paint)
        drawContext.canvas.nativeCanvas.drawText("ЮЗ", center.x - offsetDiagonal, center.y + offsetDiagonal, paint)
        drawContext.canvas.nativeCanvas.drawText("СЗ", center.x - offsetDiagonal, center.y - offsetDiagonal, paint)
    }
}