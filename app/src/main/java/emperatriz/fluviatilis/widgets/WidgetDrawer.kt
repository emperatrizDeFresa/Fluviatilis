package emperatriz.fluviatilis.widgets

import android.content.Context
import android.graphics.Canvas

interface WidgetDrawer{

    fun init(context: Context, x: Int, y: Int, width:Int, height: Int )

    fun resize(width:Int, height: Int )

    fun refresh(width:Int, height: Int )

    fun draw(canvas: Canvas, isWallpaper:Boolean,  x: Int, y: Int)

    fun isInitialized():Boolean
}