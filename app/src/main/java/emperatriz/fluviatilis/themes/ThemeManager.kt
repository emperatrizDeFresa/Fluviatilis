package emperatriz.fluviatilis.themes

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import emperatriz.fluviatilis.model.WallpaperModel
import emperatriz.fluviatilis.widgets.pypots.SysPypots
import emperatriz.fluviatilis.widgets.spin.SpinDrawUtils
import java.io.FileOutputStream


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
                    widgetSelected = 1
                    style = preferences.getInt(SysPypots.SETTINGS_DIVISIONES, 0)
                }
                "spin" -> {
                    widgetSelected = 2
                    style = preferences.getInt("outmode", 0)
                    color1 = preferences.getInt(SpinDrawUtils.ACCENT_COLOR, Color.BLACK)
                }
                "custom" -> {
                    widgetSelected = 3
                    color1 = preferences.getInt("colorAgujas", Color.BLACK)
                    color2 = preferences.getInt("colorEsfera", Color.WHITE)
                    color3 = preferences.getInt("colorBorde", Color.DKGRAY)
                    color4 = preferences.getInt("colorSegundo", Color.RED)
                }
            }

            val theme = Theme(model.fluvHeight, model.fluvNumber, model.fluvWeight, model.speed, model.wideness, preferences.getInt("heightness", 15), preferences.getInt("dimAlpha", 125),
                    preferences.getInt("dimHeight", 100), preferences.getInt("rotation", 0), preferences.getInt("horizontalOffset", 0), preferences.getInt("verticalOffset", 0),
                    preferences.getInt("color", Color.BLACK), preferences.getInt("colorLeft", 0xff333333.toInt()), preferences.getInt("colorRight", 0xff666666.toInt()), widgetSelected,
                    Math.round(preferences.getInt("widgetX", 0) * vProportion), Math.round(preferences.getInt("widgetY", 0) * vProportion), Math.round(preferences.getInt("widgetXsize", 454) * vProportion),
                    style, preferences.getBoolean(SysPypots.SETTINGS_DISCRETO, false), preferences.getBoolean(SysPypots.SETTINGS_HALO, false), color1, color2, color3, color4)

            return theme
        }

        fun getTheme(context: Context, position: Int, height: Int, width: Int): Theme?{
            try{
                val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                var themeString = preferences.getString("theme$position", THEMES.get(position-1))
                val ret =  Gson().fromJson<Theme>(themeString, Theme::class.java)
                return ret
            }catch (ex: Exception){
                return null
            }
        }

        fun getModel(context: Context, position: Int): WallpaperModel?{
            try{
                val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                var modelString = preferences.getString("model$position", MODELS.get(position-1))
                val ret =  Gson().fromJson<WallpaperModel>(modelString, WallpaperModel::class.java)
                return ret
            }catch (ex: Exception){
                return null
            }
        }

        val THEME1=""
        val THEME2=""
        val THEME3=""
        val THEME4=""
        val THEME5="{\"color\":-31196,\"colorLeft\":-6710887,\"colorRight\":-9868951,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":0,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":78,\"fluvNumber\":12,\"fluvWeight\":68,\"halo\":true,\"heightness\":39,\"horizontalOffset\":0,\"rotation\":9,\"speed\":3,\"style\":0,\"verticalOffset\":0,\"wideness\":332,\"widgetSelected\":1,\"widgetX\":165,\"widgetXsize\":178,\"widgetY\":163}"
        val THEME6=""
        val THEME7="{\"color\":-13369590,\"colorLeft\":-16174336,\"colorRight\":-16376064,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":154,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":30,\"fluvNumber\":12,\"fluvWeight\":9,\"halo\":true,\"heightness\":15,\"horizontalOffset\":0,\"rotation\":90,\"speed\":4,\"style\":0,\"verticalOffset\":0,\"wideness\":729,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":170,\"widgetY\":156}"
        val THEME8=""
        val THEME9="{\"color\":-62811,\"colorLeft\":-9895362,\"colorRight\":-16777216,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":154,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":18,\"fluvNumber\":93,\"fluvWeight\":8,\"halo\":true,\"heightness\":69,\"horizontalOffset\":0,\"rotation\":41,\"speed\":2,\"style\":0,\"verticalOffset\":0,\"wideness\":114,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":170,\"widgetY\":156}"

        val MODEL1=""
        val MODEL2=""
        val MODEL3=""
        val MODEL4=""
        val MODEL5="{\"fluvHeight\":78,\"fluvHeightDef\":80,\"fluvNumber\":12,\"fluvWeight\":68,\"fluvs\":[{\"direction\":-1.0,\"max\":182,\"min\":65,\"size\":75.54},{\"direction\":1.0,\"max\":221,\"min\":90,\"size\":169.4598},{\"direction\":-1.0,\"max\":321,\"min\":86,\"size\":179.54034},{\"direction\":1.0,\"max\":363,\"min\":224,\"size\":284.45993},{\"direction\":1.0,\"max\":281,\"min\":42,\"size\":275.45984},{\"direction\":1.0,\"max\":368,\"min\":266,\"size\":324.4603},{\"direction\":1.0,\"max\":246,\"min\":128,\"size\":177.82976},{\"direction\":1.0,\"max\":205,\"min\":107,\"size\":164.45981},{\"direction\":1.0,\"max\":307,\"min\":178,\"size\":248.45966},{\"direction\":1.0,\"max\":198,\"min\":43,\"size\":99.46003},{\"direction\":1.0,\"max\":333,\"min\":195,\"size\":203.84027},{\"direction\":1.0,\"max\":406,\"min\":308,\"size\":378.4603}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":502,\"min\":38,\"speed\":3,\"wideness\":332,\"width\":1080}"
        val MODEL6=""
        val MODEL7="{\"fluvHeight\":30,\"fluvHeightDef\":80,\"fluvNumber\":12,\"fluvWeight\":9,\"fluvs\":[{\"direction\":-1.0,\"max\":497,\"min\":438,\"size\":463.90027},{\"direction\":-1.0,\"max\":440,\"min\":350,\"size\":360.77966},{\"direction\":-1.0,\"max\":631,\"min\":109,\"size\":627.7816},{\"direction\":1.0,\"max\":714,\"min\":532,\"size\":549.61926},{\"direction\":-1.0,\"max\":633,\"min\":181,\"size\":606.46094},{\"direction\":-1.0,\"max\":652,\"min\":453,\"size\":588.8196},{\"direction\":-1.0,\"max\":220,\"min\":124,\"size\":159.66005},{\"direction\":-1.0,\"max\":313,\"min\":281,\"size\":297.30005},{\"direction\":-1.0,\"max\":520,\"min\":67,\"size\":504.7803},{\"direction\":1.0,\"max\":688,\"min\":207,\"size\":402.66058},{\"direction\":-1.0,\"max\":473,\"min\":388,\"size\":397.85974},{\"direction\":1.0,\"max\":498,\"min\":452,\"size\":479.2201}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":522,\"min\":18,\"speed\":4,\"wideness\":729,\"width\":1080}"
        val MODEL8=""
        val MODEL9="{\"fluvHeight\":18,\"fluvHeightDef\":80,\"fluvNumber\":93,\"fluvWeight\":8,\"fluvs\":[{\"direction\":1.0,\"max\":62,\"min\":21,\"size\":23.809956},{\"direction\":-1.0,\"max\":110,\"min\":5,\"size\":57.809948},{\"direction\":1.0,\"max\":86,\"min\":7,\"size\":6.930056},{\"direction\":-1.0,\"max\":98,\"min\":55,\"size\":69.76998},{\"direction\":1.0,\"max\":113,\"min\":43,\"size\":68.11014},{\"direction\":1.0,\"max\":81,\"min\":16,\"size\":34.470043},{\"direction\":1.0,\"max\":55,\"min\":34,\"size\":38.31003},{\"direction\":1.0,\"max\":105,\"min\":49,\"size\":97.79006},{\"direction\":1.0,\"max\":92,\"min\":71,\"size\":73.51003},{\"direction\":-1.0,\"max\":90,\"min\":47,\"size\":63.130028},{\"direction\":1.0,\"max\":121,\"min\":92,\"size\":119.48995},{\"direction\":-1.0,\"max\":103,\"min\":20,\"size\":62.909912},{\"direction\":1.0,\"max\":74,\"min\":29,\"size\":30.729975},{\"direction\":-1.0,\"max\":97,\"min\":73,\"size\":73.85006},{\"direction\":-1.0,\"max\":51,\"min\":32,\"size\":49.26998},{\"direction\":1.0,\"max\":51,\"min\":31,\"size\":49.449978},{\"direction\":1.0,\"max\":37,\"min\":5,\"size\":7.2700367},{\"direction\":1.0,\"max\":80,\"min\":6,\"size\":68.80998},{\"direction\":1.0,\"max\":119,\"min\":98,\"size\":106.870094},{\"direction\":1.0,\"max\":110,\"min\":80,\"size\":109.189995},{\"direction\":-1.0,\"max\":62,\"min\":20,\"size\":44.93003},{\"direction\":1.0,\"max\":93,\"min\":52,\"size\":73.60998},{\"direction\":1.0,\"max\":120,\"min\":91,\"size\":117.44993},{\"direction\":-1.0,\"max\":36,\"min\":9,\"size\":28.149921},{\"direction\":1.0,\"max\":107,\"min\":85,\"size\":88.51011},{\"direction\":-1.0,\"max\":114,\"min\":46,\"size\":77.98987},{\"direction\":-1.0,\"max\":45,\"min\":19,\"size\":19.050032},{\"direction\":-1.0,\"max\":70,\"min\":26,\"size\":30.409983},{\"direction\":1.0,\"max\":63,\"min\":21,\"size\":29.729939},{\"direction\":1.0,\"max\":112,\"min\":90,\"size\":95.55015},{\"direction\":-1.0,\"max\":78,\"min\":60,\"size\":66.94997},{\"direction\":-1.0,\"max\":83,\"min\":2,\"size\":35.929955},{\"direction\":-1.0,\"max\":112,\"min\":7,\"size\":49.77006},{\"direction\":-1.0,\"max\":57,\"min\":6,\"size\":50.51},{\"direction\":1.0,\"max\":53,\"min\":35,\"size\":38.470028},{\"direction\":1.0,\"max\":83,\"min\":23,\"size\":37.67004},{\"direction\":-1.0,\"max\":111,\"min\":78,\"size\":81.77001},{\"direction\":1.0,\"max\":70,\"min\":18,\"size\":48.489994},{\"direction\":-1.0,\"max\":73,\"min\":14,\"size\":59.950047},{\"direction\":1.0,\"max\":83,\"min\":10,\"size\":56.210003},{\"direction\":1.0,\"max\":111,\"min\":33,\"size\":65.19007},{\"direction\":-1.0,\"max\":108,\"min\":35,\"size\":62.989906},{\"direction\":1.0,\"max\":70,\"min\":27,\"size\":69.23002},{\"direction\":1.0,\"max\":48,\"min\":11,\"size\":20.670013},{\"direction\":1.0,\"max\":67,\"min\":26,\"size\":31.150002},{\"direction\":1.0,\"max\":64,\"min\":22,\"size\":48.92999},{\"direction\":1.0,\"max\":58,\"min\":27,\"size\":41.069992},{\"direction\":-1.0,\"max\":77,\"min\":59,\"size\":65.94997},{\"direction\":1.0,\"max\":55,\"min\":4,\"size\":47.289925},{\"direction\":1.0,\"max\":112,\"min\":17,\"size\":84.25002},{\"direction\":1.0,\"max\":67,\"min\":31,\"size\":53.269974},{\"direction\":-1.0,\"max\":53,\"min\":34,\"size\":49.02998},{\"direction\":-1.0,\"max\":105,\"min\":3,\"size\":58.829895},{\"direction\":1.0,\"max\":96,\"min\":17,\"size\":83.21003},{\"direction\":1.0,\"max\":77,\"min\":39,\"size\":50.389988},{\"direction\":-1.0,\"max\":106,\"min\":71,\"size\":87.96999},{\"direction\":1.0,\"max\":93,\"min\":58,\"size\":75.16995},{\"direction\":-1.0,\"max\":71,\"min\":8,\"size\":14.809997},{\"direction\":1.0,\"max\":82,\"min\":55,\"size\":75.10995},{\"direction\":-1.0,\"max\":26,\"min\":8,\"size\":14.950023},{\"direction\":-1.0,\"max\":87,\"min\":6,\"size\":23.769987},{\"direction\":-1.0,\"max\":91,\"min\":30,\"size\":86.349945},{\"direction\":1.0,\"max\":73,\"min\":14,\"size\":52.969997},{\"direction\":1.0,\"max\":92,\"min\":59,\"size\":67.82997},{\"direction\":-1.0,\"max\":102,\"min\":50,\"size\":97.66995},{\"direction\":1.0,\"max\":112,\"min\":55,\"size\":56.74999},{\"direction\":1.0,\"max\":110,\"min\":52,\"size\":96.150116},{\"direction\":1.0,\"max\":94,\"min\":31,\"size\":75.75006},{\"direction\":-1.0,\"max\":96,\"min\":8,\"size\":92.749954},{\"direction\":1.0,\"max\":40,\"min\":18,\"size\":38.77006},{\"direction\":-1.0,\"max\":33,\"min\":9,\"size\":21.02995},{\"direction\":1.0,\"max\":109,\"min\":85,\"size\":108.14994},{\"direction\":1.0,\"max\":107,\"min\":77,\"size\":105.27001},{\"direction\":1.0,\"max\":102,\"min\":33,\"size\":80.210106},{\"direction\":1.0,\"max\":105,\"min\":26,\"size\":49.710064},{\"direction\":-1.0,\"max\":82,\"min\":62,\"size\":66.76993},{\"direction\":-1.0,\"max\":114,\"min\":90,\"size\":98.45002},{\"direction\":1.0,\"max\":85,\"min\":32,\"size\":54.73001},{\"direction\":1.0,\"max\":65,\"min\":37,\"size\":43.789974},{\"direction\":1.0,\"max\":111,\"min\":54,\"size\":82.15009},{\"direction\":-1.0,\"max\":130,\"min\":105,\"size\":117.03006},{\"direction\":1.0,\"max\":65,\"min\":44,\"size\":54.47001},{\"direction\":1.0,\"max\":93,\"min\":18,\"size\":47.590042},{\"direction\":-1.0,\"max\":99,\"min\":15,\"size\":91.4299},{\"direction\":-1.0,\"max\":109,\"min\":70,\"size\":87.87005},{\"direction\":-1.0,\"max\":98,\"min\":53,\"size\":77.149994},{\"direction\":1.0,\"max\":107,\"min\":85,\"size\":91.390114},{\"direction\":1.0,\"max\":110,\"min\":10,\"size\":17.210087},{\"direction\":1.0,\"max\":58,\"min\":23,\"size\":25.23},{\"direction\":1.0,\"max\":102,\"min\":21,\"size\":48.49003},{\"direction\":1.0,\"max\":102,\"min\":18,\"size\":64.17001},{\"direction\":1.0,\"max\":110,\"min\":75,\"size\":103.789986},{\"direction\":-1.0,\"max\":103,\"min\":39,\"size\":52.76994}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":538,\"min\":2,\"speed\":2,\"wideness\":114,\"width\":1080}"

        val THEMES = listOf<String>(THEME1,THEME2,THEME3,THEME4,THEME5,THEME6,THEME7,THEME8,THEME9)
        val MODELS = listOf<String>(MODEL1,MODEL2,MODEL3,MODEL4,MODEL5,MODEL6,MODEL7,MODEL8,MODEL9)

        fun getThemeToLoad(context: Context, position: Int, height: Int, width: Int): Theme?{
            try{
                val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                val dm = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getRealMetrics(dm)
                val hProportion = width / (1f*dm.widthPixels)
                val vProportion = height / (1f*dm.heightPixels)
                var themeString = preferences.getString("theme$position", THEMES.get(position-1))
                val ret =  Gson().fromJson<Theme>(themeString, Theme::class.java)
                //ret.wideness = Math.round(ret.wideness*hProportion)
                return ret
            }catch (ex: Exception){
                return null
            }
        }

        fun getModelToLoad(context: Context, position: Int): WallpaperModel?{
            try{
                val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
                var modelString = preferences.getString("model$position", MODELS.get(position-1))
                val ret =  Gson().fromJson<WallpaperModel>(modelString, WallpaperModel::class.java)
                //ret.wideness = Math.round(ret.wideness*hProportion)
                return ret
            }catch (ex: Exception){
                return null
            }
        }

        fun saveTheme(context: Context, position: Int, theme: Theme, model: WallpaperModel){
            val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
            preferences.edit().putString("theme$position", Gson().toJson(theme)).apply()
            preferences.edit().putString("model$position", Gson().toJson(model)).apply()
        }

        fun loadTheme(context: Context, theme: Theme){
            val preferences = context.getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)

            preferences.edit().putInt("color", theme.color).apply()
            preferences.edit().putInt("wideness", theme.wideness).apply()
            preferences.edit().putInt("fluvWeight", theme.fluvWeight).apply()
            preferences.edit().putInt("fluvNumber", theme.fluvNumber).apply()
            preferences.edit().putInt("fluvHeight", theme.fluvHeight).apply()
            preferences.edit().putInt("colorLeft", theme.colorLeft).apply()
            preferences.edit().putInt("colorRight", theme.colorRight).apply()
            preferences.edit().putInt("dimAlpha", theme.dimAlpha).apply()
            preferences.edit().putInt("dimHeight", theme.dimHeight).apply()
            preferences.edit().putInt("heightness", theme.heightness).apply()
            preferences.edit().putInt("horizontalOffset", theme.horizontalOffset).apply()
            preferences.edit().putInt("rotation", theme.rotation).apply()
            preferences.edit().putInt("speed", theme.speed).apply()
            preferences.edit().putInt("verticalOffset", theme.verticalOffset).apply()
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