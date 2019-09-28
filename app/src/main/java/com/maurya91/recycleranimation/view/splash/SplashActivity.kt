package com.maurya91.recycleranimation.view.splash

import android.animation.*
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.maurya91.recycleranimation.R
import com.maurya91.recycleranimation.view.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashImage.let {
            SpringAnimation(it, DynamicAnimation.TRANSLATION_Y, 0f).apply {
                //Setting the damping ratio to create a low bouncing effect.
                spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
                //Setting the spring with a low stiffness.
                spring.stiffness = SpringForce.STIFFNESS_LOW
                // Compute velocity in the unit pixel/second
                setStartVelocity(dpToPixel(10000f))
                start()
                addEndListener { animation, canceled, value, velocity ->
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }
            }
        }

    }
    private fun dpToPixel(dp:Float): Float{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun getHeight(): Int {
        return resources.displayMetrics.heightPixels-200
    }
    private fun testAnim(){
        val xTrans = ObjectAnimator.ofFloat(splashImage, View.TRANSLATION_X, 100f)
        val yTrans = ObjectAnimator.ofFloat(splashImage, View.TRANSLATION_Y, 100f)
        // Bouncing animation with squash and stretch
        val startY = splashImage.y
        val endY = getHeight() - 50f
        val h = getHeight().toFloat()
        val d = (500 * ((h - 200) / h)).toLong()
        val bounceAnim = ObjectAnimator.ofFloat(splashImage, "y", startY, endY)
            .apply {
                duration = d
                interpolator = AccelerateInterpolator()
            }

        val squashAnim1 = ObjectAnimator.ofFloat(
            splashImage, "x", splashImage.x,
            splashImage.x - 25f
        ).apply {
            duration = d / 4
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            interpolator = DecelerateInterpolator()
        }

        val squashAnim2 = ObjectAnimator.ofFloat(
            splashImage,
            "width",
            splashImage.width.toFloat(),
            splashImage.width + 50f
        )
            .apply {
                repeatCount = 1
                repeatMode = ValueAnimator.REVERSE
                interpolator = DecelerateInterpolator()
            }

        val stretchAnim1 = ObjectAnimator.ofFloat(
            splashImage, "y", endY,
            endY + 25f
        ).apply {
            duration = d / 4
            repeatCount = 1
            interpolator = DecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
        }

        val stretchAnim2 = ObjectAnimator.ofFloat(
            splashImage, "height",
            splashImage.height.toFloat(), splashImage.height - 25f
        ).apply {
            repeatCount = 1
            interpolator = DecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
        }

        val bounceBackAnim = ObjectAnimator.ofFloat(
            splashImage, "y", endY,
            startY
        ).apply {
            duration = d
            interpolator = DecelerateInterpolator()
        }

        val bouncer = AnimatorSet().apply {
            play(bounceAnim).before(squashAnim1)
            play(squashAnim1).with(squashAnim2)
            play(squashAnim1).with(stretchAnim1)
            play(squashAnim1).with(stretchAnim2)
            play(bounceBackAnim).after(stretchAnim2)
        }
        val fadeAnim = ObjectAnimator.ofFloat(splashImage, View.ALPHA, 1f, 0f).apply {
            duration = 250
        }
        AnimatorSet().apply {
            play(bouncer).before(fadeAnim)
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }
            })
        }
    }
}
