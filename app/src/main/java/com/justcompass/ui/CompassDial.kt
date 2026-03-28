package com.justcompass.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompassDial(
    rotation: Float,
    modifier: Modifier
) {

    Canvas(modifier = modifier) {

        val radius = size.minDimension / 2
        val center = center

        // Круг компаса
        drawCircle(
            color = Color(0xFFEFEFEF),
            radius = radius
        )

        // Деления
        for (i in 0 until 360 step 10) {

            val angle = Math.toRadians(i.toDouble())

            val startX =
                center.x + (radius - 20) * cos(angle).toFloat()

            val startY =
                center.y + (radius - 20) * sin(angle).toFloat()

            val endX =
                center.x + radius * cos(angle).toFloat()

            val endY =
                center.y + radius * sin(angle).toFloat()

            drawLine(
                Color.DarkGray,
                Offset(startX, startY),
                Offset(endX, endY),
                3f
            )
        }

        // КРАСНАЯ ЗАСЕЧКА НА СЕВЕРЕ
        val northAngle = Math.toRadians(0.0)

        val northStartX =
            center.x + (radius - 30) * cos(northAngle).toFloat()

        val northStartY =
            center.y + (radius - 30) * sin(northAngle).toFloat()

        val northEndX =
            center.x + radius * cos(northAngle).toFloat()

        val northEndY =
            center.y + radius * sin(northAngle).toFloat()

        drawLine(
            color = Color.Red,
            start = Offset(northStartX, northStartY),
            end = Offset(northEndX, northEndY),
            strokeWidth = 6f
        )

        // БУКВЫ
        drawContext.canvas.nativeCanvas.apply {

            val paintDefault = android.graphics.Paint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 60f
                color = android.graphics.Color.BLACK
            }

            val paintNorth = android.graphics.Paint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 60f
                color = android.graphics.Color.RED // Север красный
            }

            // Север (красный)
            drawText("С", center.x, center.y - radius + 60, paintNorth)

            // Остальные
            drawText("В", center.x + radius - 60, center.y, paintDefault)
            drawText("Ю", center.x, center.y + radius - 20, paintDefault)
            drawText("З", center.x - radius + 60, center.y, paintDefault)
        }

        // Стрелка
        rotate(rotation, pivot = center) {

            val path = Path()

            path.moveTo(center.x, center.y - radius * 0.7f)
            path.lineTo(center.x - 12, center.y)
            path.lineTo(center.x + 12, center.y)
            path.close()

            drawPath(path, Color.Red)
        }

        // Центр
        drawCircle(Color.Black, 8f, center)
    }
}