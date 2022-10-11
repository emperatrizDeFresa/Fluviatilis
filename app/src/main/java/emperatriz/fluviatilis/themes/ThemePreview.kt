package emperatriz.fluviatilis.themes

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import emperatriz.fluviatilis.liveWallpaper.R
import emperatriz.fluviatilis.model.WallpaperModel
import emperatriz.fluviatilis.widgets.WidgetDrawer
import emperatriz.fluviatilis.widgets.custom.CustomDrawer
import emperatriz.fluviatilis.widgets.pypots.PypotsDrawer
import emperatriz.fluviatilis.widgets.spin.SpinDrawUtils
import emperatriz.fluviatilis.widgets.spin.SpinDrawer
import java.util.*


/**
 * This allows the settings activity to show a preview of the wallpaper to the user
 */
class ThemePreview @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {


    constructor(context: Context, attrs: AttributeSet? = null, theme: Theme, _model: WallpaperModel, llHeight:Int=0, llWidth:Int=0) : this(context, attrs, 0) {

        val dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getRealMetrics(dm)
        val hProportion = llWidth / (1f*dm.widthPixels)
        val vProportion = llHeight / (1f*dm.heightPixels)

        this.theme = theme

        pypotsWidget = PypotsDrawer()
        spinWidget = SpinDrawer()
        customWidget = CustomDrawer()
        this.gradientTop = BitmapFactory.decodeResource(context.getResources(), R.drawable.gradient)
        this.gradientBottom = BitmapFactory.decodeResource(context.getResources(), R.drawable.gradient2)

        if (theme.fluvNumber==0){
            theme.fluvNumber=14
        }

        model = WallpaperModel(_model.fluvHeight, _model.min, _model.max, _model.fluvNumber, _model.fluvWeight, _model.fps, _model.isWallpaper, _model.height, _model.width, _model.speed, _model.wideness)

        model.wideness = Math.round(theme.wideness*vProportion)
        model.fluvWeight = Math.round(theme.fluvWeight*vProportion)
        model.fluvNumber = theme.fluvNumber
        model.fluvHeight = Math.round(llHeight * (theme.heightness / 100f)) / model.fluvNumber//Math.round(theme.fluvHeight*vProportion)

        theme.dimHeight = Math.round(theme.dimHeight*vProportion)
        theme.wideness = Math.round(theme.wideness*hProportion)
        theme.heightness = Math.round(theme.heightness*vProportion)
        theme.fluvWeight = Math.round(theme.fluvWeight*vProportion)
        theme.fluvHeight = Math.round(theme.fluvHeight*vProportion)
        theme.horizontalOffset = Math.round(theme.horizontalOffset*vProportion)
        theme.verticalOffset = Math.round(theme.verticalOffset*vProportion)

        if (theme.mode==1){
            model.initFluvs()
        }
        else{
            model.initBars(theme.colorLeft, theme.color, theme.colorRight)
        }

    }
    lateinit var gradientTop: Bitmap
    lateinit var gradientBottom: Bitmap
    lateinit var theme:Theme
    lateinit var model:WallpaperModel
    lateinit var pypotsWidget : PypotsDrawer
    lateinit var spinWidget : SpinDrawer
    lateinit var customWidget : CustomDrawer
    var color: Int = 0
    var colorRight: Int = 0
    var colorLeft: Int = 0
    var hOffset: Int = 0
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private var paintArc = Paint()
    private var paintAlpha = Paint()

    private val paintLeft = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val paintRight = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val paintLeft2 = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val paintRight2 = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }


    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)


        val largePath = Path()
        largePath.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(),30f,30f,Path.Direction.CCW)
        canvas.clipPath(largePath)

        hOffset = height

        lateinit var widget:WidgetDrawer
        if (theme.widgetSelected>0) {
            when(theme.widgetSelected){
                1 -> {
                    widget = pypotsWidget
                }
                2 -> {
                    widget = spinWidget
                }
                3 -> {
                    widget = customWidget
                }
            }
        }

        if (theme.mode==1){

            val verticalOffset = theme.verticalOffset
            val horizontalOffset = theme.horizontalOffset
            canvas.save()
            canvas.translate(horizontalOffset.toFloat(), verticalOffset.toFloat())

            canvas.rotate(theme.rotation.toFloat(), (width / 2).toFloat(), ((height) / 2).toFloat())

            //model.fluvHeight = Math.round(((height) * (theme.heightness.toFloat() / 100f)) / theme.fluvNumber).toInt()
            model.fluvNumber = theme.fluvNumber
            model.speed = 0
            model.fluvWeight = theme.fluvWeight
            model.fps=60
            model.wideness = theme.wideness
            color = theme.color
            paintArc.color = color
            paintArc.isAntiAlias = true
            colorRight = theme.colorRight
            colorLeft = theme.colorLeft
            paint.color = color
            paintLeft.color=colorLeft
            paintLeft2.color=colorLeft
            paintRight2.color=colorRight
            paintRight.color=colorRight


            canvas.drawColor(Color.BLACK)

            if (model.fluvWeight!=0){
                paintArc.style = Paint.Style.STROKE
                paintArc.strokeWidth = model.fluvWeight.toFloat()

                canvas.drawRect((-height - hOffset).toFloat(), -hOffset * 1f, width / 2f, (height + hOffset * 1f).toFloat(), paintLeft)
                canvas.drawRect((width / 2).toFloat(), -hOffset * 1f, (height + hOffset).toFloat(), (height + hOffset * 1f).toFloat(), paintRight)

                paintLeft.strokeWidth = model.fluvHeight.toFloat()
                paintRight.strokeWidth = model.fluvHeight.toFloat()

                var y = height/2-((model.fluvHeight*model.fluvNumber)+model.fluvWeight)/2
                var i = 0
                drawFirstBackground(canvas, y.toFloat(), true)
                model.fluvs.forEach{
                    if (i++%2==0){
                        drawFluvBackground(canvas, Rect(width / 2, y, (width / 2 + it.size).toInt(), y + model.fluvHeight), true)
                    }
                    else{
                        drawFluvBackground(canvas, Rect((width / 2 - it.size).toInt(), y, width / 2, y + model.fluvHeight), false)
                    }

                    y += model.fluvHeight
                }
                drawLastBackground(canvas, y.toFloat(), i % 2 != 0)
                y = height/2-((model.fluvHeight*model.fluvNumber)+model.fluvWeight)/2
                i = 0
                drawFirst(canvas, y.toFloat(), true)
                model.fluvs.forEach{
                    if (i++%2==0){
                        drawFluv(canvas, Rect((width / 2)-1, y, (width / 2 + it.size).toInt(), y + model.fluvHeight), true, i == 1, i == model.fluvNumber)
                    }
                    else{
                        drawFluv(canvas, Rect((width / 2 - it.size).toInt(), y, (width / 2)+1, y + model.fluvHeight), false, i == 1, i == model.fluvNumber)
                    }

                    y += model.fluvHeight
                }
                drawLast(canvas, y.toFloat(), i % 2 != 0)



                canvas.rotate(theme.rotation.toFloat(), (width / 2).toFloat(), ((height - hOffset) / 2).toFloat())

                canvas.restore()

                val shaderHeight=theme.dimHeight
                paintAlpha.alpha = theme.dimAlpha
                canvas.drawBitmap(gradientTop, null, RectF(0F, 0F, width.toFloat(), shaderHeight.toFloat()), paintAlpha)
                canvas.drawBitmap(gradientBottom, null, RectF(0F, ((height) - shaderHeight).toFloat(), width.toFloat(), (height).toFloat()), paintAlpha)






            }
        }
        else{
            hOffset = height

            canvas.save()
            //canvas.translate(horizontalOffset.toFloat(),verticalOffset.toFloat())

            canvas.rotate(theme.rotation.toFloat(),width*0.5f,height*0.5f)


            model.fluvHeight = Math.round((height / theme.fluvNumber).toDouble()).toInt()
            model.fluvNumber = theme.fluvNumber
            model.speed = theme.speed
            model.fluvWeight = theme.fluvWeight
            model.fps=60
            model.wideness = 0
            val color_ = theme.color
            val colorRight_ = theme.colorRight
            val colorLeft_ = theme.colorLeft
            paint.color = color



            canvas.drawColor(Color.BLACK)

            if (model.fluvWeight!=0) {

                var y = height / -2//-((model.fluvHeight*model.fluvNumber)+model.fluvWeight)/2
                var i = 0
                model.bars.forEach {
                    paint.color = it.actualColor
                    canvas.drawRect(-1f * width, y * 1f - 2, width * 2f, y * 1f +model.fluvHeight +2 , paint)

                    y += Math.round(model.fluvHeight*1f)
                }




                canvas.rotate(-theme.rotation.toFloat(), width * 0.5f, height * 0.5f)

                canvas.restore()

                val shaderHeight = theme.dimHeight
                paintAlpha.alpha = theme.dimAlpha
                canvas.drawBitmap(gradientTop, null, RectF(0F, 0F, width.toFloat(), shaderHeight.toFloat()), paintAlpha)
                canvas.drawBitmap(gradientBottom, null, RectF(0F, ((height) - shaderHeight).toFloat(), width.toFloat(), (height).toFloat()), paintAlpha)


            }
        }


    }


    fun drawFluvBackground(canvas: Canvas, rect: Rect, left: Boolean){
        if (!left){
            canvas.drawLine(0F, (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), rect.right.toFloat(), (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), paintLeft)
            canvas.drawLine((rect.left).toFloat(), (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), width.toFloat(), (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), paintRight)
        }
        else{
            canvas.drawLine((rect.left).toFloat(), (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), width.toFloat(), (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), paintRight)
            canvas.drawLine(0F, (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), rect.right.toFloat(), (((rect.top + rect.bottom) / 2) + model.fluvWeight / 2).toFloat(), paintLeft)
        }

    }

    fun drawFirst(canvas: Canvas, y: Float, left: Boolean){
        val diameter = model.fluvHeight+model.fluvWeight
        canvas.drawRect((width.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), -hOffset * 1f, (width.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), y - diameter / 2 + model.fluvWeight.toFloat() / 2 + 1, paint)
        canvas.drawArc((width.toFloat() / 2).toFloat(), y + model.fluvWeight.toFloat() / 2 - diameter, (width.toFloat() / 2 + diameter).toFloat() + 1, y + model.fluvWeight.toFloat() / 2, -180F, -90F, false, paintArc)
    }

    fun drawFirstBackground(canvas: Canvas, y: Float, left: Boolean){
        val radius = (model.fluvHeight+model.fluvWeight)/2
        canvas.drawRect(0F, -hOffset / 2f, (width.toFloat() / 2 + radius).toFloat(), y + model.fluvWeight, paintLeft2)
        canvas.drawRect((width.toFloat() / 2).toFloat(), -hOffset / 2f, width.toFloat(), y - radius + model.fluvWeight, paintRight2)
        canvas.drawLine((width.toFloat() / 2 + radius).toFloat(), y - radius + model.fluvWeight, width.toFloat(), y - radius + model.fluvWeight, paintRight)
    }

    fun drawLast(canvas: Canvas, y: Float, left: Boolean){
        val diameter = model.fluvHeight+model.fluvWeight
        canvas.drawRect((width.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), y + diameter / 2 + model.fluvWeight.toFloat() / 2 - 1, (width.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), height + hOffset.toFloat(), paint)
        if (left){
            canvas.drawArc((width.toFloat() / 2).toFloat(), y + model.fluvWeight.toFloat() / 2, (width.toFloat() / 2 + diameter).toFloat() + 1, y + diameter + model.fluvWeight.toFloat() / 2, 270F, -90F, false, paintArc)
        }
        else{
            canvas.drawArc((width.toFloat() / 2 - diameter).toFloat() - 1, y + model.fluvWeight.toFloat() / 2, (width.toFloat() / 2).toFloat(), y + diameter + model.fluvWeight.toFloat() / 2, 270F, 90F, false, paintArc)
        }

    }

    fun drawLastBackground(canvas: Canvas, y: Float, left: Boolean){
        val radius = (model.fluvHeight+model.fluvWeight)/2

        if (left){
            canvas.drawRect(0F, y, (width / 2 + radius).toFloat(), height.toFloat(), paintLeft2)
            canvas.drawRect((width / 2).toFloat(), y + radius, width.toFloat(), height.toFloat(), paintRight2)
            canvas.drawLine(width / 2 + radius.toFloat(), y + radius, width.toFloat(), y + radius, paintRight)
        }
        else{
            canvas.drawRect((width / 2 - radius).toFloat(), y, width.toFloat(), height.toFloat(), paintRight2)
            canvas.drawRect(0F, y + radius, (width / 2).toFloat(), height.toFloat(), paintLeft2)
            canvas.drawLine(0F, y + radius, width / 2 - radius.toFloat(), y + radius, paintLeft)
        }
    }

    fun drawFluv(canvas: Canvas, rect: Rect, left: Boolean, first: Boolean, last: Boolean){
        val diameter:Float = (model.fluvHeight+model.fluvWeight).toFloat()
        val c = 0f

        if (first){
            if (left){
                canvas.drawRect((rect.left + diameter / 2).toFloat(), rect.top.toFloat(), rect.right.toFloat(), (rect.top + model.fluvWeight).toFloat(), paint)
            }
            else{
                canvas.drawRect(rect.left.toFloat(), rect.top.toFloat(), (rect.right - diameter / 2).toFloat(), (rect.top + model.fluvWeight).toFloat(), paint)
            }
        }
        else{
            canvas.drawRect(rect.left.toFloat()-c, rect.top.toFloat(), rect.right.toFloat()+c, (rect.top + model.fluvWeight).toFloat(), paint)
        }
        if (last){
            if (left){
                canvas.drawRect((rect.left + diameter / 2).toFloat(), rect.bottom.toFloat(), rect.right.toFloat(), (rect.bottom + model.fluvWeight).toFloat(), paint)
            }
            else{
                canvas.drawRect(rect.left.toFloat(), rect.bottom.toFloat(), (rect.right - diameter / 2).toFloat(), (rect.bottom + model.fluvWeight).toFloat(), paint)
            }
        }
        else{
            canvas.drawRect(rect.left.toFloat()-c, rect.bottom.toFloat(), rect.right.toFloat()+c, (rect.bottom + model.fluvWeight).toFloat(), paint)
        }


        if (!left){
            canvas.drawArc((rect.left - diameter.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + model.fluvWeight.toFloat() / 2).toFloat(), (rect.left + diameter.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + diameter - model.fluvWeight.toFloat() / 2).toFloat(), 89F, 182F, false, paintArc)
        }
        else{
            canvas.drawArc((rect.right - diameter.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + model.fluvWeight.toFloat() / 2).toFloat(), (rect.right + diameter.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + diameter - model.fluvWeight.toFloat() / 2).toFloat(), -91F, 182F, false, paintArc)
        }


    }

}
