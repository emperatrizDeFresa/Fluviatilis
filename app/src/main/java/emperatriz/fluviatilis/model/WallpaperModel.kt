package emperatriz.fluviatilis.model

import android.content.SharedPreferences
import android.graphics.Color
import java.util.*

data class Fluv(var size: Float, var direction: Float, var min: Int, var max: Int)
data class Bar(var size: Float, var actualSuperColor: SuperColor, var actualColor: Int, var baseColor: Int, var destinationColor: Int, var directionR: Int =1, var directionG: Int =1, var directionB: Int =1)

data class SuperColor(var red:Double, var green: Double, var blue:Double)

data class WallpaperModel(var fluvHeight:Int,
                          val min: Int,
                          val max: Int,
                          var fluvNumber: Int,
                          var fluvWeight: Int,
                          var fps:Int,
                          val isWallpaper:Boolean,
                          var height:Int,
                          val width:Int,
                          var speed:Int,
                          var wideness:Int){

    val fluvHeightDef=80
    var fluvs: MutableList<Fluv> = mutableListOf<Fluv>()
    var bars: MutableList<Bar> = mutableListOf<Bar>()

    fun initFluvs(){
        if (fluvs==null){
            fluvs = mutableListOf<Fluv>()
        }
        fluvs?.let{it.clear()}
        for (x in 1..fluvNumber){
            try{
                var maxWidth = wideness//Math.min(wideness,max)
                if (maxWidth < min+fluvHeight){
                    maxWidth = min+fluvHeight
                }
                val min_ = (min..maxWidth).random()
                val max_ = (min..maxWidth).random()
                if (min_> max_){
                    fluvs.add(Fluv((max_..min_).random().toFloat(),(-1..1).random().toFloat(),max_, min_))
                }
                else{
                    fluvs.add(Fluv((min_..max_).random().toFloat(),(-1..1).random().toFloat(),min_,max_))
                }


            }
            catch (e: Exception) {
                fluvs.add(Fluv(1F,speed.toFloat(),fluvHeight,380))
            }
        }
        fluvs.forEach{
            if (it.max-it.min<fluvHeight*1){
                if (it.max+fluvHeight*1<max){
                    it.max = it.max+fluvHeight*1
                }
                else{
                    it.min = it.min-fluvHeight*1
                }
            }
        }
    }


    fun updateFluvs(preferences: SharedPreferences){

        var lu = preferences.getLong("lastUpdate",0L)
        var delta = Calendar.getInstance().time.time-lu

        if (delta<100){
            fluvs.forEach{
                if (it.direction==0F){
                    it.direction=1F
                }
                if (speed<=5){
                    it.size += it.direction*speed/200*delta
                }
                else{
                    it.size += it.direction*speed/50*delta
                }

                if (it.size>it.max){
                    it.direction = -Math.abs(it.direction)
                }
                if (it.size<it.min){
                    it.direction = Math.abs(it.direction)
                }
            }
        }
        preferences.edit().putLong("lastUpdate",Calendar.getInstance().time.time).apply()
    }



    fun maxFluvWeight():Int{
        return if (fluvHeight==0) fluvHeightDef-10 else fluvHeight-10
    }

    fun maxFluvNumber():Int{
        return 400
    }

    fun maxSpeed():Int{
        return 10
    }

    fun maxWideness():Int{
        return Math.round(width/2-width*0.1).toInt()
    }

    fun initBars(color1: Int, color2: Int, color3:Int){
        if (bars==null){
            bars = mutableListOf<Bar>()
        }
        bars?.let{it.clear()}
        for (x in 1..fluvNumber*2){
            val color = if (x%3==0) color1 else if (x%3==1) color2 else color3
            val color_ = if (x%3==0) color2 else if (x%3==1) color3 else color1
            bars.add(Bar(fluvHeight *1f, SuperColor(Color.red(color).toDouble(),Color.green(color).toDouble(),Color.blue(color).toDouble()),color, color, color_))
        }
    }

    fun updateBars(preferences: SharedPreferences){

        var lu = preferences.getLong("lastUpdate",0L)
        var delta = Calendar.getInstance().time.time-lu

        if (delta<100 && speed>1){
            bars.forEach{
                it.actualSuperColor = calculateColor(it, 500L/speed)
                it.actualColor = Color.rgb(it.actualSuperColor.red.toInt(),it.actualSuperColor.green.toInt(),it.actualSuperColor.blue.toInt())
                if (it.actualSuperColor.red == Color.red(it.destinationColor).toDouble()){
                    it.directionR = -1
                }
                if (it.actualSuperColor.red== Color.red(it.baseColor).toDouble()){
                    it.directionR = 1
                }
                if (it.actualSuperColor.green == Color.green(it.destinationColor).toDouble()){
                    it.directionG = -1
                }
                if (it.actualSuperColor.green== Color.green(it.baseColor).toDouble()){
                    it.directionG = 1
                }
                if (it.actualSuperColor.blue == Color.blue(it.destinationColor).toDouble()){
                    it.directionB = -1
                }
                if (it.actualSuperColor.blue== Color.blue(it.baseColor).toDouble()){
                    it.directionB = 1
                }
            }
        }
        preferences.edit().putLong("lastUpdate",Calendar.getInstance().time.time).apply()
    }

    private fun calculateColor(it: Bar, steps: Long): SuperColor {
        var red = it.actualSuperColor.red
        var redBase = Color.red(it.baseColor)
        var redDest = Color.red(it.destinationColor)
        var redRange = Math.abs(redBase-redDest).toDouble()
        var redStep: Double = ((redRange/steps).toDouble())
        if (redBase<redDest){
            red += redStep*it.directionR
        }
        else{
            red -= redStep*it.directionR
        }
        red = Math.max(Math.min(redBase,redDest).toDouble(),red)
        red = Math.min(Math.max(redBase,redDest).toDouble(),red)

        var green = it.actualSuperColor.green
        var greenBase = Color.green(it.baseColor)
        var greenDest = Color.green(it.destinationColor)
        var greenRange = Math.abs(greenBase-greenDest).toDouble()
        var greenStep:Double = (greenRange/steps).toDouble()
        if (greenBase<greenDest){
            green += greenStep*it.directionG
        }
        else{
            green -= greenStep*it.directionG
        }
        green = Math.max(Math.min(greenBase,greenDest).toDouble(),green)
        green = Math.min(Math.max(greenBase,greenDest).toDouble(),green)

        var blue = it.actualSuperColor.blue
        var blueBase = Color.blue(it.baseColor)
        var blueDest = Color.blue(it.destinationColor)
        var blueRange = Math.abs(blueBase-blueDest).toDouble()
        var blueStep:Double = (blueRange/steps).toDouble()
        if (blueBase<blueDest){
            blue += blueStep*it.directionB
        }
        else{
            blue -= blueStep*it.directionB
        }
        blue = Math.max(Math.min(blueBase,blueDest).toDouble(),blue)
        blue = Math.min(Math.max(blueBase,blueDest).toDouble(),blue)

        return SuperColor(red,green,blue)
    }


}

