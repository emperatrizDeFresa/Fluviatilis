package emperatriz.fluviatilis.model

import android.content.Context
import android.content.SharedPreferences
import java.util.*

data class Fluv(var size: Float, var direction: Float, var min: Int, var max: Int)

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

}

