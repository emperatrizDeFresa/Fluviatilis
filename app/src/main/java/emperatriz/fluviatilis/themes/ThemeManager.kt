package emperatriz.fluviatilis.themes

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.DisplayMetrics
import com.google.gson.Gson
import emperatriz.fluviatilis.model.WallpaperModel
import emperatriz.fluviatilis.widgets.pypots.SysPypots
import emperatriz.fluviatilis.widgets.spin.SpinDrawUtils

class ThemeManager {

    object companion{

        fun getCurrentTheme(context: Context, model: WallpaperModel, height: Int, width: Int): Theme{
            val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
            val dm = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getRealMetrics(dm)
            val hProportion = width / (1f*dm.widthPixels)
            val vProportion = height / (1f*dm.heightPixels)
            var margin = Math.round(preferences.getInt("fluvWeight", 12) * 2 * hProportion)
//            val model = WallpaperModel(Math.round((height * (preferences.getInt("heightness", 15)).toFloat() / 100) / preferences.getInt("fluvNumber", 16)).toInt(),
//                    margin,
//                    (width / 2) - margin,
//                    Math.round(preferences.getInt("fluvNumber", 16) * 1f),
//                    Math.round(preferences.getInt("fluvWeight", 12) * 1f),
//                    60,
//                    false,
//                    height,
//                    width,
//                    preferences.getInt("speed", 2),
//                    Math.round(preferences.getInt("wideness", 380) * 1f))


            var widgetSelected=0
            var style=0
            var color1=0
            var color2=0
            var color3=0
            var color4=0

            when (preferences.getString("selectedWidget", "pypots")){
                "pypots" -> {
                    widgetSelected=1
                    style = preferences.getInt(SysPypots.SETTINGS_DIVISIONES, 0)
                }
                "spin"   -> {
                    widgetSelected=2
                    style = preferences.getInt("outmode", 0)
                    color1 = preferences.getInt(SpinDrawUtils.ACCENT_COLOR, Color.BLACK)
                }
                "custom" -> {
                    widgetSelected=3
                    color1 = preferences.getInt("colorAgujas",  Color.BLACK)
                    color2 = preferences.getInt("colorEsfera",  Color.WHITE)
                    color3 = preferences.getInt("colorBorde",  Color.DKGRAY)
                    color4 = preferences.getInt("colorSegundo",  Color.RED)
                }
            }

            val theme = Theme(model.fluvHeight, model.fluvNumber, model.fluvWeight, model.speed, Math.round(model.wideness*1f), Math.round(preferences.getInt("heightness", 15) * 1f), preferences.getInt("dimAlpha", 125), Math.round(preferences.getInt("dimHeight", 100) * 1f),
                    preferences.getInt("rotation", 0), Math.round(preferences.getInt("horizontalOffset", 0) * 1f), Math.round(preferences.getInt("verticalOffset", 0) * 1f), preferences.getInt("color", Color.BLACK),
                    preferences.getInt("colorLeft", Color.rgb(50, 50, 50)), preferences.getInt("colorRight", Color.rgb(100, 100, 100)), widgetSelected,
                    Math.round(preferences.getInt("widgetX", 0)* vProportion), Math.round(preferences.getInt("widgetY", 0)* vProportion), Math.round(preferences.getInt("widgetXsize", 454)* vProportion),
                    style, preferences.getBoolean(SysPypots.SETTINGS_DISCRETO, false),preferences.getBoolean(SysPypots.SETTINGS_HALO, false), color1, color2, color3, color4)

            return theme
        }

        fun getTheme(context: Context, position: Int, height: Int, width: Int): Theme?{
            try{
                val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                val dm = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getRealMetrics(dm)
                val hProportion = width / (1f*dm.widthPixels)
                val vProportion = height / (1f*dm.heightPixels)
                var themeString = preferences.getString("theme$position","")
                val ret =  Gson().fromJson<Theme>(themeString,Theme::class.java)
                ret.wideness = Math.round(ret.wideness*hProportion)
                return ret
            }catch (ex:Exception){
                return null
            }
        }

        fun getThemeToLoad(context: Context, position: Int, height: Int, width: Int): Theme?{
            try{
                val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                val dm = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getRealMetrics(dm)
                val hProportion = width / (1f*dm.widthPixels)
                val vProportion = height / (1f*dm.heightPixels)
                var themeString = preferences.getString("theme$position","")
                val ret =  Gson().fromJson<Theme>(themeString,Theme::class.java)
                //ret.wideness = Math.round(ret.wideness*hProportion)
                return ret
            }catch (ex:Exception){
                return null
            }
        }

        fun getModelToLoad(context: Context, position: Int): WallpaperModel?{
            try{
                val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                var modelString = preferences.getString("model$position","")
                val ret =  Gson().fromJson<WallpaperModel>(modelString,WallpaperModel::class.java)
                //ret.wideness = Math.round(ret.wideness*hProportion)
                return ret
            }catch (ex:Exception){
                return null
            }
        }

        fun saveTheme(context: Context, position: Int, theme: Theme, model:WallpaperModel){
            val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
            preferences.edit().putString("theme$position",Gson().toJson(theme)).apply()
            preferences.edit().putString("model$position",Gson().toJson(model)).apply()
        }

        fun loadTheme(context: Context,  theme: Theme){
            val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)

            preferences.edit().putInt("color",theme.color).apply()
            preferences.edit().putInt("wideness",theme.wideness).apply()
            preferences.edit().putInt("fluvWeight",theme.fluvWeight).apply()
            preferences.edit().putInt("fluvNumber",theme.fluvNumber).apply()
            preferences.edit().putInt("fluvHeight",theme.fluvHeight).apply()
            preferences.edit().putInt("colorLeft",theme.colorLeft).apply()
            preferences.edit().putInt("colorRight",theme.colorRight).apply()
            preferences.edit().putInt("dimAlpha",theme.dimAlpha).apply()
            preferences.edit().putInt("dimHeight",theme.dimHeight).apply()
            preferences.edit().putInt("heightness",theme.heightness).apply()
            preferences.edit().putInt("horizontalOffset",theme.horizontalOffset).apply()
            preferences.edit().putInt("rotation",theme.rotation).apply()
            preferences.edit().putInt("speed",theme.speed).apply()
            preferences.edit().putInt("verticalOffset",theme.verticalOffset).apply()
//            preferences.edit().putInt("widgetSelected",theme.widgetSelected).apply()
//            preferences.edit().putInt("widgetXsize",theme.widgetXsize!!).apply()
//            preferences.edit().putInt("widgetX",theme.widgetX!!).apply()
//            preferences.edit().putInt("colorW1",theme.colorW1!!).apply()
//            preferences.edit().putInt("colorW2",theme.colorW2!!).apply()
//            preferences.edit().putInt("colorW3",theme.colorW3!!).apply()
//            preferences.edit().putInt("colorW4",theme.colorW4!!).apply()
//            preferences.edit().putInt("style",theme.style!!).apply()
//            preferences.edit().putInt("widgetY",theme.widgetY!!).apply()
//            preferences.edit().putBoolean("discreto",theme.discreto!!).apply()
//            preferences.edit().putBoolean("halo",theme.halo!!).apply()

        }
    }


}