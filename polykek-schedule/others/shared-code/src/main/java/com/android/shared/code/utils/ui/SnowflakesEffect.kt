package com.android.shared.code.utils.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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
 * @constructor Create [SnowflakesEffect]
 */
class SnowflakesEffect(private val context: Context) {
    private val particlePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val particleThinPaint: Paint
    private val bitmapPaint = Paint()
    private var particleBitmap: Bitmap? = null
    private var lastAnimationTime: Long = 0
    private val angleDiff = (Math.PI / 180 * 60).toFloat()

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
                    var angle = -Math.PI.toFloat() / 2
                    if (particleBitmap == null) {
                        particleThinPaint.alpha = 255

                        particleBitmap = Bitmap.createBitmap(16.toDp(), 16.toDp(), Bitmap.Config.ARGB_8888)
                        val bitmapCanvas = Canvas(particleBitmap!!)
                        val px: Float = 4.0f.toDpf2()
                        val px1: Float = (-1.14f).toDpf2()
                        val py1: Float = 3.1f.toDpf2()
                        var a = 0
                        while (a < 6) {
                            val x: Float = 8.toDpf()
                            val y: Float = 8.toDpf()
                            var x1 = cos(angle.toDouble()).toFloat() * px
                            var y1 = sin(angle.toDouble()).toFloat() * px
                            val cx = x1 * 0.66f
                            val cy = y1 * 0.66f
                            bitmapCanvas.drawLine(x, y, x + x1, y + y1, particleThinPaint)
                            val angle2 = (angle - Math.PI / 2).toFloat()
                            x1 = (cos(angle2.toDouble()) * px1 - sin(angle2.toDouble()) * py1).toFloat()
                            y1 = (sin(angle2.toDouble()) * px1 + cos(angle2.toDouble()) * py1).toFloat()
                            bitmapCanvas.drawLine(x + cx, y + cy, x + x1, y + y1, particleThinPaint)
                            x1 = (-cos(angle2.toDouble()) * px1 - sin(angle2.toDouble()) * py1).toFloat()
                            y1 = (-sin(angle2.toDouble()) * px1 + cos(angle2.toDouble()) * py1).toFloat()
                            bitmapCanvas.drawLine(x + cx, y + cy, x + x1, y + y1, particleThinPaint)
                            angle += angleDiff
                            a++
                        }
                    }
                    bitmapPaint.alpha = (255 * alpha).toInt()
                    canvas.save()
                    canvas.scale(scale, scale, x, y)
                    canvas.drawBitmap(particleBitmap!!, x, y, bitmapPaint)
                    canvas.restore()
                }
                else -> {
                    var angle = -Math.PI.toFloat() / 2
                    if (particleBitmap == null) {
                        particleThinPaint.alpha = 255
                        particleBitmap = Bitmap.createBitmap(16.toDp(), 16.toDp(), Bitmap.Config.ARGB_8888)
                        val bitmapCanvas = Canvas(particleBitmap!!)
                        val px: Float = 4f.toDpf2()
                        val px1: Float = (-1.14f).toDpf2()
                        val py1: Float = 3.1f.toDpf2()
                        var a = 0
                        while (a < 6) {
                            val x: Float = 8.toDpf()
                            val y: Float = 8.toDpf()
                            var x1 = cos(angle.toDouble()).toFloat() * px
                            var y1 = sin(angle.toDouble()).toFloat() * px
                            val cx = x1 * 0.66f
                            val cy = y1 * 0.66f
                            bitmapCanvas.drawLine(x, y, x + x1, y + y1, particleThinPaint)
                            val angle2 = (angle - Math.PI / 2).toFloat()
                            x1 = (((cos(angle2.toDouble()) * px1) - (sin(angle2.toDouble()) * py1))).toFloat()
                            y1 = (((sin(angle2.toDouble()) * px1) + (cos(angle2.toDouble()) * py1))).toFloat()
                            bitmapCanvas.drawLine(x + cx, y + cy, x + x1, y + y1, particleThinPaint)
                            x1 = (-cos(angle2.toDouble()) * px1 - sin(angle2.toDouble()) * py1).toFloat()
                            y1 = (-sin(angle2.toDouble()) * px1 + cos(angle2.toDouble()) * py1).toFloat()
                            bitmapCanvas.drawLine(x + cx, y + cy, x + x1, y + y1, particleThinPaint)
                            angle += angleDiff
                            a++
                        }
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
        particlePaint.color = 0xFFFFFF
        particleThinPaint.color = 0xFFFFFF
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
                if (freeParticles.size < 40) {
                    freeParticles.add(particle)
                }
                particles.removeAt(a)
                a--
                count--
                a++
                continue
            }
            if (particle.currentTime < 200.0f) {
                particle.alpha = accelerateInterpolator.getInterpolation(particle.currentTime / 200.0f)
            } else {
                particle.alpha =
                    1.0f - decelerateInterpolator.getInterpolation((particle.currentTime - 200.0f) / (particle.lifeTime - 200.0f))
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
        val maxCount = 100
        val createPerFrame = 1
        if (particles.size < maxCount) {
            for (i in 0 until createPerFrame) {
                if (particles.size < maxCount && random.nextFloat() > 0.7f) {
                    val statusBarHeight = 0//AndroidUtilities.statusBarHeight
                    val cx: Float = random.nextFloat() * parent.measuredWidth
                    val cy: Float =
                        statusBarHeight + random.nextFloat() * (parent.measuredHeight - 20.toDp() - statusBarHeight)
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
                    newParticle.lifeTime = 2000f + random.nextInt(100)
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

    private fun Int.toDpf(): Float = ceil(this * context.resources.displayMetrics.density)

    private fun Float.toDpf2(): Float = this * context.resources.displayMetrics.density

    private fun Float.toDpf(): Float = ceil(this * context.resources.displayMetrics.density)

    companion object {
        private val accelerateInterpolator = AccelerateInterpolator()
        private val decelerateInterpolator = DecelerateInterpolator()
        private val random = Random()
    }
}