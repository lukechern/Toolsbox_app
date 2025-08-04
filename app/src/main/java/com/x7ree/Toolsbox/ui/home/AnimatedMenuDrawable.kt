package com.x7ree.Toolsbox.ui.home

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.animation.DecelerateInterpolator

class AnimatedMenuDrawable(private val width: Int, private val height: Int) : Drawable() {

    private val paint = Paint()
    private var progress = 0f

    init {
        paint.isAntiAlias = true
        paint.strokeWidth = 6f
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun getIntrinsicWidth(): Int = width
    override fun getIntrinsicHeight(): Int = height

    override fun draw(canvas: Canvas) {
        drawMenuOrClose(canvas, progress)
    }

    private fun drawMenuOrClose(canvas: Canvas, progress: Float) {
        val centerX = width / 2f
        val centerY = height / 2f

        // Fade out hamburger lines
        val menuAlpha = ((1 - progress) * 255).toInt()
        paint.alpha = menuAlpha

        val lineLength = 36f
        val halfLine = lineLength / 2
        val gap = 12f

        // Top line
        val topY = lerp(centerY - gap, centerY, progress)
        val topX1 = lerp(centerX - halfLine, centerX, progress)
        val topX2 = lerp(centerX + halfLine, centerX, progress)
        canvas.drawLine(topX1, topY, topX2, topY, paint)

        // Middle line
        canvas.drawLine(centerX - halfLine, centerY, centerX + halfLine, centerY, paint)

        // Bottom line
        val bottomY = lerp(centerY + gap, centerY, progress)
        val bottomX1 = lerp(centerX - halfLine, centerX, progress)
        val bottomX2 = lerp(centerX + halfLine, centerX, progress)
        canvas.drawLine(bottomX1, bottomY, bottomX2, bottomY, paint)

        // Fade in X icon
        val crossAlpha = (progress * 255).toInt()
        if (crossAlpha > 0) {
            paint.alpha = crossAlpha
            val crossSize = 18f

            // Rotate top and bottom lines to form the X
            canvas.save()
            canvas.rotate(lerp(0f, 45f, progress), centerX, centerY)
            canvas.drawLine(centerX - crossSize, centerY, centerX + crossSize, centerY, paint)
            canvas.restore()

            canvas.save()
            canvas.rotate(lerp(0f, -45f, progress), centerX, centerY)
            canvas.drawLine(centerX - crossSize, centerY, centerX + crossSize, centerY, paint)
            canvas.restore()
        }

        paint.alpha = 255
    }

    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setProgress(newProgress: Float) {
        progress = newProgress
        invalidateSelf()
    }

    fun getProgress(): Float = progress
}