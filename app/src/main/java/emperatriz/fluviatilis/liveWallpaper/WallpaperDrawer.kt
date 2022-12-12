package emperatriz.fluviatilis.liveWallpaper

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import emperatriz.fluviatilis.model.WallpaperModel
import emperatriz.fluviatilis.themes.Theme
import emperatriz.fluviatilis.widgets.WidgetDrawer
import emperatriz.fluviatilis.widgets.custom.CustomDrawer
import emperatriz.fluviatilis.widgets.pypots.PypotsDrawer
import emperatriz.fluviatilis.widgets.spin.SpinDrawer
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class WallpaperDrawer : WallpaperRenderer {


    var hOffset:Int = 0
    lateinit var context: Context
    var isWallpaper: Boolean = false
    lateinit var widget: WidgetDrawer
    lateinit var pypotsWidget : PypotsDrawer
    lateinit var spinWidget : SpinDrawer
    lateinit var customWidget : CustomDrawer
    var verticalOffset:Int=0
    var horizontalOffset:Int=0

    constructor(context: Context, isWallpaper: Boolean) {
        this.context=context
        this.isWallpaper=isWallpaper
        this.gradientTop = BitmapFactory.decodeResource(context.getResources(), R.drawable.gradient)
        this.gradientBottom = BitmapFactory.decodeResource(context.getResources(), R.drawable.gradient2)
        this.preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
        color = preferences.getInt("color", Color.BLACK)
        colorRight = preferences.getInt("colorRight", Color.rgb(100, 100, 100))
        colorLeft = preferences.getInt("colorLeft", Color.rgb(50, 50, 50))

        color_ = preferences.getInt("color_", Color.BLACK)
        colorRight_ = preferences.getInt("colorRight_", Color.rgb(100, 100, 100))
        colorLeft_ = preferences.getInt("colorLeft_", Color.rgb(50, 50, 50))

        pypotsWidget = PypotsDrawer()
        spinWidget = SpinDrawer()
        customWidget = CustomDrawer()

        if (preferences.getBoolean("showWidget", false)) {
            when(preferences.getString("selectedWidget", "pypots")){
                "pypots" ->{
                    widget = pypotsWidget
                }
                "spin" ->{
                    widget = spinWidget
                }
                "custom" ->{
                    widget = customWidget
                }
            }
            widget.init(context, preferences.getInt("widgetX", 0), preferences.getInt("widgetY", 0), preferences.getInt("widgetXsize", 454), preferences.getInt("widgetXsize", 454))
        }

        verticalOffset = preferences.getInt("verticalOffset",0)
        horizontalOffset = preferences.getInt("horizontalOffset",0)


    }

    lateinit var gradientTop: Bitmap
    lateinit var gradientBottom: Bitmap
    lateinit var preferences: SharedPreferences

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val paint_ = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private var paintArc = Paint()
    private var paintAlpha = Paint()
    private var paintAlpha_ = Paint()

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

    var color: Int
        get() = paint.color
        set(value) {
            paint.color = value
            paintArc.color = value
        }
    var colorRight: Int
    var colorLeft: Int

    var colorLeft_: Int
    var color_: Int
    var colorRight_: Int


    var model: WallpaperModel = WallpaperModel(0, 0, 0, 0, 0, 0, isWallpaper, 0, 0, 0, 380)
    private var height: Int = 0
    private var width: Int = 0

    init {

    }

    override fun setCanvasSize(width: Int, height: Int) {

            val fluvheight = Math.round((height * (preferences.getInt("heightness", 15)).toFloat() / 100) / preferences.getInt("fluvNumber", 16)).toInt()
            paintArc.style = Paint.Style.STROKE
            paintArc.strokeWidth = preferences.getInt("fluvWeight", 12).toFloat()
            paintArc.isAntiAlias = true
            paintLeft.strokeWidth = fluvheight.toFloat()+1
            paintLeft.color=colorLeft
            paintRight.strokeWidth = fluvheight.toFloat()+1
            paintRight.color=colorRight
            paintLeft2.strokeWidth = 0F
            paintLeft2.color=colorLeft
            paintRight2.strokeWidth = 0F
            paintRight2.color=colorRight

            if (preferences.getInt("mode",1)==1){
                this.width = width
                this.height = height//+hOffset
                var margin = preferences.getInt("fluvWeight", 12)*2
                model = WallpaperModel(fluvheight,
                        margin,
                        (height / 2) - margin,
                        preferences.getInt("fluvNumber", 16),
                        preferences.getInt("fluvWeight", 12),
                        60,
                        isWallpaper,
                        height,
                        height,
                        preferences.getInt("speed", 2),
                        preferences.getInt("wideness", 380))
//            paintArc.style = Paint.Style.STROKE
//            paintArc.strokeWidth = preferences.getInt("fluvWeight", 12).toFloat()
//            paintArc.isAntiAlias = true
//            paintLeft.strokeWidth = fluvheight.toFloat()+1
//            paintLeft.color=colorLeft
//            paintRight.strokeWidth = fluvheight.toFloat()+1
//            paintRight.color=colorRight
//            paintLeft2.strokeWidth = 0F
//            paintLeft2.color=colorLeft
//            paintRight2.strokeWidth = 0F
//            paintRight2.color=colorRight
                model.initFluvs()
            }
            else{
                this.width = width
                this.height = height
                color_ = preferences.getInt("color_", Color.BLACK)
                colorRight_ = preferences.getInt("colorRight_", Color.rgb(100, 100, 100))
                colorLeft_ = preferences.getInt("colorLeft_", Color.rgb(50, 50, 50))
                model = WallpaperModel(preferences.getInt("fluvNumber_", 20),
                        4,
                        100,
                        3,
                        preferences.getInt("fluvWeight_", 12),
                        60,
                        isWallpaper,
                        width,
                        height,
                        preferences.getInt("speed_", 2),
                        0)
                model.initBars(colorLeft_, color_, colorRight_)
            }
    }

    fun refresh(theme: Theme){

    }

    var lastUpdated:Boolean=false

    override fun draw(canvas: Canvas) {

            if (preferences.getInt("mode",1)==1){
                hOffset = height


                if (preferences.getBoolean("showWidget", false)) {
                    when(preferences.getString("selectedWidget", "pypots")){
                        "pypots" ->{
                            widget = pypotsWidget
                        }
                        "spin" ->{
                            widget = spinWidget
                        }
                        "custom" ->{
                            widget = customWidget
                        }
                    }
                }

                val verticalOffset = preferences.getInt("verticalOffset",0)
                val horizontalOffset = preferences.getInt("horizontalOffset",0)
                canvas.save()
                canvas.translate(horizontalOffset.toFloat(),verticalOffset.toFloat())

                canvas.rotate(preferences.getInt("rotation", 0).toFloat(),(width/2).toFloat(),((height)/2).toFloat())

                model.fluvHeight = Math.round(((height) * (preferences.getInt("heightness", 15)).toFloat() / 100) / preferences.getInt("fluvNumber", 16)).toInt()
                model.fluvNumber = preferences.getInt("fluvNumber", 16)
                model.speed = preferences.getInt("speed", 2)
                model.fluvWeight = preferences.getInt("fluvWeight", 12)
                model.fps=60
                model.wideness = preferences.getInt("wideness", 380)
                color = preferences.getInt("color", Color.BLACK)
                colorRight = preferences.getInt("colorRight", Color.rgb(100, 100, 100))
                colorLeft = preferences.getInt("colorLeft", Color.rgb(50, 50, 50))
                paint.color = color
                paintLeft.color=colorLeft
                paintLeft2.color=colorLeft
                paintRight2.color=colorRight
                paintRight.color=colorRight

                if (preferences.getBoolean("changed", false)){
                    model.initFluvs()
                    preferences.edit().putBoolean("changed", false).apply()
                }
                if (isWallpaper && preferences.getBoolean("changedWallpaper", false)){
                    var margin = preferences.getInt("fluvWeight", 12)*2
                    model = WallpaperModel(Math.round((height * (preferences.getInt("heightness", 15)).toFloat() / 100) / preferences.getInt("fluvNumber", 16)).toInt(),
                            margin,
                            (width / 2) - margin,
                            preferences.getInt("fluvNumber", 16),
                            preferences.getInt("fluvWeight", 12),
                            60,
                            isWallpaper,
                            height,
                            width,
                            preferences.getInt("speed", 2),
                            preferences.getInt("wideness", 380))
                    model.initFluvs()
                    preferences.edit().putBoolean("changedWallpaper", false).apply()
                }


                canvas.drawColor(Color.BLACK)

                if (model.fluvWeight!=0){
                    paintArc.strokeWidth = model.fluvWeight.toFloat()
                    paintLeft.strokeWidth = 0f
                    paintRight.strokeWidth = 0f

                    canvas.drawRect((-height-hOffset).toFloat(),-hOffset*1f,width/2f,(height+hOffset*1f).toFloat(),paintLeft)
                    canvas.drawRect((width/2).toFloat(),-hOffset*1f,(height+hOffset).toFloat(),(height+hOffset*1f).toFloat(),paintRight)

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
                            drawFluv(canvas, Rect((width / 2)-2, y, (width / 2 + it.size).toInt(), y + model.fluvHeight), true, i == 1, i == model.fluvNumber)
                        }
                        else{
                            drawFluv(canvas, Rect((width / 2 - it.size).toInt(), y, (width / 2)+2, y + model.fluvHeight), false, i == 1, i == model.fluvNumber)
                        }

                        y += model.fluvHeight
                    }
                    drawLast(canvas, y.toFloat(), i % 2 != 0)



                    canvas.rotate(-preferences.getInt("rotation", 0).toFloat(),(width/2).toFloat(),((height-hOffset)/2).toFloat())

                    canvas.restore()

                    val shaderHeight=preferences.getInt("dimHeight", 100)
                    paintAlpha.alpha = preferences.getInt("dimAlpha", 125)
                    canvas.drawBitmap(gradientTop, null, RectF(0F, 0F, width.toFloat(), shaderHeight.toFloat()), paintAlpha)
                    canvas.drawBitmap(gradientBottom, null, RectF(0F, ((height)-shaderHeight).toFloat(), width.toFloat(), (height).toFloat()), paintAlpha)



                    if (preferences.getLong("lastLocation",0L)>4*60*60*1000){
                        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener {
                                it?.let {
                                    preferences.edit().putLong("latitude",it.latitude.toLong())
                                    preferences.edit().putLong("longitude",it.longitude.toLong())
                                    preferences.edit().putLong("lastLocation",Calendar.getInstance().time.time)
                                }
                            }
                        }
                    }



                    if (preferences.getBoolean("showWidget", false)){

                        val widgetX =  preferences.getInt("widgetX", 0)// - preferences.getInt("widgetXsize", 454) / 2
                        val widgetY =  preferences.getInt("widgetY", 0)// - preferences.getInt("widgetXsize", 454) / 2
                        if (!widget.isInitialized()){
                            widget.init(context, widgetX, widgetY, preferences.getInt("widgetXsize", 454), preferences.getInt("widgetXsize", 454))
                        }
                        if (isWallpaper && preferences.getBoolean("changedWidgetWallpaper", false)){
                            widget.init(context, widgetX,widgetY,preferences.getInt("widgetXsize", 454),preferences.getInt("widgetXsize", 454))
                            preferences.edit().putBoolean("changedWidgetWallpaper", false).apply()
                        }
                        if (!isWallpaper && preferences.getBoolean("changedWidget", false)){
                            widget.init(context, widgetX,widgetY,preferences.getInt("widgetXsize", 454),preferences.getInt("widgetXsize", 454))
                            preferences.edit().putBoolean("changedWidget", false).apply()
                        }

                        widget.draw(canvas, isWallpaper, widgetX, widgetY)
                    }


                    model.updateFluvs(preferences)

                }

            }
            else{

                hOffset = height

                if (preferences.getBoolean("showWidget", false)) {
                    when(preferences.getString("selectedWidget", "pypots")){
                        "pypots" ->{
                            widget = pypotsWidget
                        }
                        "spin" ->{
                            widget = spinWidget
                        }
                        "custom" ->{
                            widget = customWidget
                        }
                    }
                }

                canvas.save()

                canvas.rotate(preferences.getInt("rotation_", 0).toFloat(),width*0.5f,height*0.5f)


                model.fluvHeight = preferences.getInt("fluvNumber_", 20)
                model.fluvNumber = 3
                model.speed = preferences.getInt("speed_", 2)
                model.fluvWeight = preferences.getInt("fluvWeight_", 12)
                model.fps=60
                model.wideness = 0
                color_ = preferences.getInt("color_", Color.BLACK)
                colorRight_ = preferences.getInt("colorRight_", Color.rgb(100, 100, 100))
                colorLeft_ = preferences.getInt("colorLeft_", Color.rgb(50, 50, 50))
                paint_.color = color

                if (preferences.getBoolean("changed", false)){
                    model.initBars(colorLeft_, color_, colorRight_)
                    preferences.edit().putBoolean("changed", false).apply()
                }
                if (isWallpaper && preferences.getBoolean("changedWallpaper", false)){
                    model = WallpaperModel(preferences.getInt("fluvNumber_", 20),
                            0,
                            100,
                            3,
                            preferences.getInt("fluvWeight_", 12),
                            60,
                            isWallpaper,
                            height,
                            width,
                            preferences.getInt("speed_", 2),
                            preferences.getInt("wideness_", 380))
                    model.initBars(colorLeft_, color_, colorRight_)
                    preferences.edit().putBoolean("changedWallpaper", false).apply()
                }


                canvas.drawColor(Color.BLACK)

                if (model.fluvWeight!=0){



                    var d = height/2
                    var u = d


                    while (d<height*2){
                        model.bars.forEach{
                            paint_.color = it.actualColor
                            canvas.drawRect(-2f*width,d*1f,width*2f,d*1f+it.size+1,paint_)

                            d += (it.size).toInt()
                        }
                    }
                    while (u>height*-2){
                        model.bars.reversed().forEach{
                            paint_.color = it.actualColor
                            canvas.drawRect(-2f*width,u*1f-it.size ,width*2f,u*1f+1,paint_)

                            u -= (it.size).toInt()
                        }
                    }

                    canvas.rotate(-preferences.getInt("rotation_", 0).toFloat(),width*0.5f,height*0.5f)

                    canvas.restore()

                    val shaderHeight=preferences.getInt("dimHeight_", 100)
                    paintAlpha_.alpha = preferences.getInt("dimAlpha_", 125)
                    canvas.drawBitmap(gradientTop, null, RectF(0F, 0F, width.toFloat(), shaderHeight.toFloat()), paintAlpha_)
                    canvas.drawBitmap(gradientBottom, null, RectF(0F, ((height)-shaderHeight).toFloat(), width.toFloat(), (height).toFloat()), paintAlpha_)



                    if (preferences.getLong("lastLocation",0L)>4*60*60*1000){
                        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener {
                                it?.let {
                                    preferences.edit().putLong("latitude",it.latitude.toLong())
                                    preferences.edit().putLong("longitude",it.longitude.toLong())
                                    preferences.edit().putLong("lastLocation",Calendar.getInstance().time.time)
                                }
                            }
                        }
                    }



                    if (preferences.getBoolean("showWidget", false)){

                        val widgetX =  preferences.getInt("widgetX", 0)// - preferences.getInt("widgetXsize", 454) / 2
                        val widgetY =  preferences.getInt("widgetY", 0)// - preferences.getInt("widgetXsize", 454) / 2
                        if (!widget.isInitialized()){
                            widget.init(context, widgetX, widgetY, preferences.getInt("widgetXsize", 454), preferences.getInt("widgetXsize", 454))
                        }
                        if (isWallpaper && preferences.getBoolean("changedWidgetWallpaper", false)){
                            widget.init(context, widgetX,widgetY,preferences.getInt("widgetXsize", 454),preferences.getInt("widgetXsize", 454))
                            preferences.edit().putBoolean("changedWidgetWallpaper", false).apply()
                        }
                        if (!isWallpaper && preferences.getBoolean("changedWidget", false)){
                            widget.init(context, widgetX,widgetY,preferences.getInt("widgetXsize", 454),preferences.getInt("widgetXsize", 454))
                            preferences.edit().putBoolean("changedWidget", false).apply()
                        }

                        widget.draw(canvas, isWallpaper, widgetX, widgetY)
                    }


                    model.updateBars(preferences)

                }
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
            canvas.drawArc((rect.left - diameter.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + model.fluvWeight.toFloat() / 2).toFloat(), (rect.left + diameter.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + diameter - model.fluvWeight.toFloat() / 2).toFloat(), 89F, 182F, false, paintArc)
        }
        else{
            canvas.drawArc((rect.right - diameter.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + model.fluvWeight.toFloat() / 2).toFloat(), (rect.right + diameter.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), (rect.top + diameter - model.fluvWeight.toFloat() / 2).toFloat(), -91F, 182F, false, paintArc)
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
        canvas.drawRect((width.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), -hOffset*1f, (width.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), y - diameter / 2 + model.fluvWeight.toFloat() / 2 + 1, paint)
        canvas.drawArc((width.toFloat() / 2).toFloat(), y + model.fluvWeight.toFloat() / 2 - diameter, (width.toFloat() / 2 + diameter).toFloat() + 1, y + model.fluvWeight.toFloat() / 2, -180F, -90F, false, paintArc)
    }

    fun drawFirstBackground(canvas: Canvas, y: Float, left: Boolean){
        val radius = (model.fluvHeight+model.fluvWeight)/2
        canvas.drawRect(0F, -hOffset/2f, (width.toFloat() / 2 + radius ).toFloat(), y + model.fluvWeight, paintLeft2)
        canvas.drawRect((width.toFloat() / 2).toFloat(), -hOffset/2f, width.toFloat(), y - radius + model.fluvWeight, paintRight2)
        canvas.drawLine((width.toFloat() / 2 + radius).toFloat(), y - radius + model.fluvWeight, width.toFloat(), y - radius + model.fluvWeight, paintRight)
    }

    fun drawLast(canvas: Canvas, y: Float, left: Boolean){
        val diameter = model.fluvHeight+model.fluvWeight
        canvas.drawRect((width.toFloat() / 2 - model.fluvWeight.toFloat() / 2).toFloat(), y + diameter / 2 + model.fluvWeight.toFloat() / 2 - 1, (width.toFloat() / 2 + model.fluvWeight.toFloat() / 2).toFloat(), height+hOffset.toFloat(), paint)
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
}