package emperatriz.fluviatilis.themes

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import emperatriz.fluviatilis.liveWallpaper.R
import emperatriz.fluviatilis.model.WallpaperModel
import emperatriz.fluviatilis.widgets.WidgetDrawer
import emperatriz.fluviatilis.widgets.custom.CustomDrawer
import emperatriz.fluviatilis.widgets.pypots.PypotsDrawer
import emperatriz.fluviatilis.widgets.spin.SpinDrawUtils
import emperatriz.fluviatilis.widgets.spin.SpinDrawer


/**
 * This allows the settings activity to show a preview of the wallpaper to the user
 */
class ThemePreview @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet? = null, theme: Theme, model: WallpaperModel) : this(context, attrs, 0) {
        this.theme = theme
        this.model = model
        pypotsWidget = PypotsDrawer()
        spinWidget = SpinDrawer()
        customWidget = CustomDrawer()
        this.gradientTop = BitmapFactory.decodeResource(context.getResources(), R.drawable.gradient)
        this.gradientBottom = BitmapFactory.decodeResource(context.getResources(), R.drawable.gradient2)

        model.initFluvs()
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

        var widget:WidgetDrawer
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

        val verticalOffset = theme.verticalOffset
        val horizontalOffset = theme.horizontalOffset
        canvas.save()
        canvas.translate(horizontalOffset.toFloat(), verticalOffset.toFloat())

        canvas.rotate(theme.rotation.toFloat(), (width / 2).toFloat(), ((height) / 2).toFloat())

        model.fluvHeight = Math.round(((height) * theme.heightness.toFloat() / 100) / theme.fluvNumber).toInt()
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
            paintLeft.strokeWidth = 0f
            paintRight.strokeWidth = 0f

            canvas.drawRect((-height - hOffset).toFloat(), -hOffset * 1f, width / 2f, (height + hOffset * 1f).toFloat(), paintLeft)
            canvas.drawRect((width / 2).toFloat(), -hOffset * 1f, (height + hOffset).toFloat(), (height + hOffset * 1f).toFloat(), paintRight)

            paintLeft.strokeWidth = model.fluvHeight.toFloat()+1
            paintRight.strokeWidth = model.fluvHeight.toFloat()+1

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
                    drawFluv(canvas, Rect(width / 2, y, (width / 2 + it.size).toInt(), y + model.fluvHeight), true, i == 1, i == model.fluvNumber)
                }
                else{
                    drawFluv(canvas, Rect((width / 2 - it.size).toInt(), y, width / 2, y + model.fluvHeight), false, i == 1, i == model.fluvNumber)
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




            if (theme.widgetSelected>0){

                val widgetX =  theme.widgetX
                val widgetY =  theme.widgetY
//                if (!widget.isInitialized()){
//                    widget.init(context, widgetX, widgetY, preferences.getInt("widgetXsize", 454), preferences.getInt("widgetXsize", 454))
//                }
//                if (isWallpaper && preferences.getBoolean("changedWidgetWallpaper", false)){
//                    widget.init(context, widgetX,widgetY,preferences.getInt("widgetXsize", 454),preferences.getInt("widgetXsize", 454))
//                    preferences.edit().putBoolean("changedWidgetWallpaper", false).apply()
//                }
//                if (!isWallpaper && preferences.getBoolean("changedWidget", false)){
//                    widget.init(context, widgetX,widgetY,preferences.getInt("widgetXsize", 454),preferences.getInt("widgetXsize", 454))
//                    preferences.edit().putBoolean("changedWidget", false).apply()
//                }
//
//                widget.draw(canvas, isWallpaper, widgetX, widgetY)
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


        if (first){
            if (left){
                canvas.drawRect((rect.left + diameter / 2).toFloat(), rect.top.toFloat(), rect.right.toFloat(), (rect.top + model.fluvWeight).toFloat(), paint)
            }
            else{
                canvas.drawRect(rect.left.toFloat(), rect.top.toFloat(), (rect.right - diameter / 2).toFloat(), (rect.top + model.fluvWeight).toFloat(), paint)
            }
        }
        else{
            canvas.drawRect(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), (rect.top + model.fluvWeight).toFloat(), paint)
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
            canvas.drawRect(rect.left.toFloat(), rect.bottom.toFloat(), rect.right.toFloat(), (rect.bottom + model.fluvWeight).toFloat(), paint)
        }


        if (!left){
            canvas.drawArc((rect.left - diameter.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + model.fluvWeight.toFloat() / 2).toFloat(), (rect.left + diameter.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + diameter - model.fluvWeight.toFloat() / 2).toFloat(), 90F, 180F, false, paintArc)
        }
        else{
            canvas.drawArc((rect.right - diameter.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + model.fluvWeight.toFloat() / 2).toFloat(), (rect.right + diameter.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + diameter - model.fluvWeight.toFloat() / 2).toFloat(), -90F, 180F, false, paintArc)
        }


    }

}
