package emperatriz.fluviatilis.liveWallpaper

import android.R.attr
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Handler
import android.provider.AlarmClock
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import java.util.*
import kotlin.math.pow


/**
 * The engine is used by the launcher to render the wallpaper
 *
 * A handler is triggered at most every 8 ms to redraw the wallpaper
 */
abstract class BaseWallpaperService : WallpaperService() {

    abstract fun createWallpaperRenderer(): WallpaperRenderer

    override fun onCreateEngine(): WallpaperService.Engine {
        return WallpaperEngine()
    }

    private inner class WallpaperEngine internal constructor() : WallpaperService.Engine() {
        private val handler = Handler()
        private val drawRunner = Runnable { draw() }

        private var visible = true
        private val wallpaperRenderer: WallpaperRenderer = createWallpaperRenderer()

        init {
            handler.removeCallbacks(drawRunner)
            handler.post(drawRunner)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                handler.removeCallbacks(drawRunner)
                handler.post(drawRunner)
            } else {
                handler.removeCallbacks(drawRunner)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            this.visible = false
            handler.removeCallbacks(drawRunner)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            //(wallpaperRenderer as WallpaperDrawer).fluvWeight = 12
            wallpaperRenderer.setCanvasSize(width, height)
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder) {
            super.onSurfaceRedrawNeeded(holder)
            draw()
        }

        private val MAX_CLICK_DURATION = 200
        private var startClickTime: Long = 0
        override fun onTouchEvent(event: MotionEvent) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val clickDuration: Long = Calendar.getInstance().getTimeInMillis() - startClickTime
                    if (clickDuration < MAX_CLICK_DURATION) {
                        val preferences = getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                        val radius = preferences.getInt("widgetXsize", 454)*0.5F
                        val x = preferences.getInt("widgetX", 0)
                        val y = preferences.getInt("widgetY", 0)


                        if ((event.rawX-x).pow(2) + (event.rawY-y).pow(2) < radius.pow(2)){
                            val i = Intent(AlarmClock.ACTION_SHOW_ALARMS)
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)
                        }

                    }
                }
                MotionEvent.ACTION_UP -> {
                    startClickTime = Calendar.getInstance().getTimeInMillis()

                }
            }
            true
        }

        private fun draw() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas() //NOTE: This method can take up to 8ms to run
                if (canvas != null) {
                    wallpaperRenderer.draw(canvas)
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas) //NOTE: This method can take up to 16ms to run
                }
            }

            handler.removeCallbacks(drawRunner)

            if (visible) {
                /**
                 * At most once per 8 ms the wallpaper will be redrawn
                 * This number can be changed to allow for faster updates (by reducing the number)
                 * or to reduce CPU and battery usage (by increasing the number)
                 */
                handler.postDelayed(drawRunner, 16L)
            }
        }
    }
}