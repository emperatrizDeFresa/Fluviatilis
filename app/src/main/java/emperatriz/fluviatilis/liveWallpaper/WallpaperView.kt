package emperatriz.fluviatilis.liveWallpaper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.AlarmClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.io.Console
import java.util.*
import java.util.logging.ConsoleHandler
import kotlin.math.pow


/**
 * This allows the settings activity to show a preview of the wallpaper to the user
 */
class WallpaperView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), Runnable {
    var renderer: WallpaperRenderer? = null
        set(value) {
            field = value
            post { renderer!!.setCanvasSize(width, height) }
            post(this)
        }

    var posted = false

    var canDragWidget = true

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!posted && renderer != null) {
            post { renderer!!.setCanvasSize(width, height) }
            post(this)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        renderer!!.draw(canvas)
    }

    override fun run() {
        posted = true
        postInvalidate()
        postDelayed(this, 32L)
    }

    var move = false
    var firstTouch = true
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (canDragWidget){
            val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
            val radius = preferences.getInt("widgetXsize", 454)*0.5F
            val x = preferences.getInt("widgetX", 0)
            val y = preferences.getInt("widgetY", 0)


            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                if (firstTouch){
                    firstTouch=false
                    if ((event.rawX-x).pow(2) + (event.rawY-y).pow(2) < radius.pow(2)){
                        move=true
                    }
                }
                else if (move){
                    preferences.edit().putInt("widgetX", Math.round(event.rawX)).apply()
                    preferences.edit().putInt("widgetY", Math.round(event.rawY)).apply()
                }
            } else if (event.action == MotionEvent.ACTION_UP) {
                move = false
                firstTouch = true
            }
        }
        return true
    }


}
