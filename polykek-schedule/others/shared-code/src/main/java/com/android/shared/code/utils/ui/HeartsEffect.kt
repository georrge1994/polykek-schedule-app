package com.android.shared.code.utils.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import java.util.*
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Snowflakes effect class is from Telegram app.
 *
 * @property context Context
 * @constructor Create [HeartsEffect]
 */
class HeartsEffect(private val context: Context) {
    private val particlePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val particleThinPaint: Paint
    private val bitmapPaint = Paint()
    private var particleBitmap: Bitmap? = null
    private var lastAnimationTime: Long = 0

    private inner class Particle {
        var x = 0f
        var y = 0f
        var vx = 0f
        var vy = 0f
        var velocity = 0f
        var alpha = 0f
        var lifeTime = 0f
        var currentTime = 0f
        var scale = 0f
        var type = 0

        fun draw(canvas: Canvas) {
            when (type) {
                0 -> {
                    particlePaint.alpha = (255 * alpha).toInt()
                    canvas.drawPoint(x, y, particlePaint)
                }
                1 -> {
                    if (particleBitmap == null) {
                        particleThinPaint.style = Paint.Style.FILL_AND_STROKE
                        particleThinPaint.alpha = 255
                        particleBitmap = Bitmap.createBitmap(12.toDp(), 12.toDp(), Bitmap.Config.ARGB_8888)
                        val bitmapCanvas = Canvas(particleBitmap!!)
                        bitmapCanvas.drawPath(getHeartPath(bitmapCanvas.width, bitmapCanvas.height), particleThinPaint)
                    }
                    bitmapPaint.alpha = (255 * alpha).toInt()
                    canvas.save()
                    canvas.scale(scale, scale, x, y)
                    canvas.drawBitmap(particleBitmap!!, x, y, bitmapPaint)
                    canvas.restore()
                }
                else -> {
                    if (particleBitmap == null) {
                        particleThinPaint.alpha = 255
                        particleBitmap = Bitmap.createBitmap(12.toDp(), 12.toDp(), Bitmap.Config.ARGB_8888)
                        val bitmapCanvas = Canvas(particleBitmap!!)
                        bitmapCanvas.drawPath(getHeartPath(bitmapCanvas.width, bitmapCanvas.height), particleThinPaint)
                    }
                    bitmapPaint.alpha = (255 * alpha).toInt()
                    canvas.save()
                    canvas.scale(scale, scale, x, y)
                    canvas.drawBitmap(particleBitmap!!, x, y, bitmapPaint)
                    canvas.restore()
                }
            }
        }
    }

    private val particles = ArrayList<Particle>()
    private val freeParticles = ArrayList<Particle>()

    init {
        particlePaint.strokeWidth = 1.5f.toDpf()
        particlePaint.strokeCap = Paint.Cap.ROUND
        particlePaint.style = Paint.Style.STROKE
        particleThinPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        particleThinPaint.strokeWidth = 0.5f.toDpf()
        particleThinPaint.strokeCap = Paint.Cap.ROUND
        particleThinPaint.style = Paint.Style.STROKE
        particlePaint.color = 0xFF0000
        particleThinPaint.color = 0xFF0000
        for (a in 0..19) {
            freeParticles.add(Particle())
        }
    }

    private fun updateParticles(dt: Long) {
        var count = particles.size
        var a = 0
        while (a < count) {
            val particle = particles[a]
            if (particle.currentTime >= particle.lifeTime) {
                if (freeParticles.size < 60) {
                    freeParticles.add(particle)
                }
                particles.removeAt(a)
                a--
                count--
                a++
                continue
            }
            if (particle.currentTime < 300.0f) {
                particle.alpha = accelerateInterpolator.getInterpolation(particle.currentTime / 300.0f)
            } else {
                particle.alpha =
                    1.0f - decelerateInterpolator.getInterpolation((particle.currentTime - 300.0f) / (particle.lifeTime - 300.0f))
            }
            particle.x += particle.vx * particle.velocity * dt / 500.0f
            particle.y += particle.vy * particle.velocity * dt / 500.0f
            particle.currentTime += dt.toFloat()
            a++
        }
    }

    fun onDraw(parent: View?, canvas: Canvas?) {
        if (parent == null || canvas == null) {
            return
        }
        val count = particles.size
        for (a in 0 until count) {
            val particle = particles[a]
            particle.draw(canvas)
        }
        val maxCount = 80
        val createPerFrame = 1
        if (particles.size < maxCount) {
            for (i in 0 until createPerFrame) {
                if (particles.size < maxCount && random.nextFloat() > 0.7f) {
                    val cx: Float = random.nextFloat() * parent.measuredWidth
                    val cy: Float = random.nextFloat() * (parent.measuredHeight - 20.toDp())
                    val angle: Int = random.nextInt(40) - 20 + 90
                    val vx = cos(Math.PI / 180.0 * angle).toFloat()
                    val vy = sin(Math.PI / 180.0 * angle).toFloat()
                    var newParticle: Particle
                    if (freeParticles.isNotEmpty()) {
                        newParticle = freeParticles[0]
                        freeParticles.removeAt(0)
                    } else {
                        newParticle = Particle()
                    }
                    newParticle.x = cx
                    newParticle.y = cy
                    newParticle.vx = vx
                    newParticle.vy = vy
                    newParticle.alpha = 0.0f
                    newParticle.currentTime = 0f
                    newParticle.scale = random.nextFloat() * 1.2f
                    newParticle.type = random.nextInt(2)
                    newParticle.lifeTime = 3000f + random.nextInt(100)
                    newParticle.velocity = 20.0f + random.nextFloat() * 4.0f
                    particles.add(newParticle)
                }
            }
        }
        val newTime = System.currentTimeMillis()
        val dt = min(17, newTime - lastAnimationTime)
        updateParticles(dt)
        lastAnimationTime = newTime
        parent.invalidate()
    }

    private fun Int.toDp(): Int = (this * context.resources.displayMetrics.density).toInt()

    private fun Float.toDpf(): Float = ceil(this * context.resources.displayMetrics.density)

    /**
     * Get heart path.
     *
     * @param width Width
     * @param height Height
     * @return [Path]
     */
    private fun getHeartPath(width: Int, height: Int): Path {
        val path = Path()
        val pX = width / 2f
        val pY = height / 100f * 33.33f

        var x1 = width / 100f * 50
        var y1 = height / 100f * 5
        var x2 = width / 100f * 90
        var y2 = height / 100f * 10
        var x3 = width / 100f * 90
        var y3 = height / 100f * 33.33f

        path.moveTo(pX, pY)
        path.cubicTo(x1, y1, x2, y2, x3, y3)
        path.moveTo(x3, pY)

        x1 = width / 100f * 90
        y1 = height / 100f * 55f
        x2 = width / 100f * 65
        y2 = height / 100f * 60f
        x3 = width / 100f * 50
        y3 = height / 100f * 90f

        path.cubicTo(x1, y1, x2, y2, x3, y3)
        path.lineTo(pX, pY)


        x1 = width / 100f * 50
        y1 = height / 100f * 5
        x2 = width / 100f * 10
        y2 = height / 100f * 10
        x3 = width / 100f * 10
        y3 = height / 100f * 33.33f

        path.moveTo(pX, pY)
        path.cubicTo(x1, y1, x2, y2, x3, y3)
        path.moveTo(x3, pY)

        x1 = width / 100f * 10
        y1 = height / 100f * 55f
        x2 = width / 100f * 35f
        y2 = height / 100f * 60f
        x3 = width / 100f * 50f
        y3 = height / 100f * 90f

        path.cubicTo(x1, y1, x2, y2, x3, y3)
        path.lineTo(pX, pY)

        path.moveTo(x3, y3)
        path.close()
        return path
    }

    private companion object {
        private val accelerateInterpolator = AccelerateInterpolator()
        private val decelerateInterpolator = DecelerateInterpolator()
        private val random = Random()
    }
}