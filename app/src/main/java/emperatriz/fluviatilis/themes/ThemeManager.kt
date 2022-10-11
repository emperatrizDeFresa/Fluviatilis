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

            var theme = Theme(preferences.getInt("mode",1),model.fluvHeight, model.fluvNumber, model.fluvWeight, model.speed, model.wideness, preferences.getInt("heightness", 15), preferences.getInt("dimAlpha", 125),
                    preferences.getInt("dimHeight", 100), preferences.getInt("rotation", 0), preferences.getInt("horizontalOffset", 0), preferences.getInt("verticalOffset", 0),
                    preferences.getInt("color", Color.BLACK), preferences.getInt("colorLeft", 0xff333333.toInt()), preferences.getInt("colorRight", 0xff666666.toInt()), widgetSelected,
                    Math.round(preferences.getInt("widgetX", 0) * vProportion), Math.round(preferences.getInt("widgetY", 0) * vProportion), Math.round(preferences.getInt("widgetXsize", 454) * vProportion),
                    style, preferences.getBoolean(SysPypots.SETTINGS_DISCRETO, false), preferences.getBoolean(SysPypots.SETTINGS_HALO, false), color1, color2, color3, color4)
            if (preferences.getInt("mode",1)!=1){
                theme = Theme(preferences.getInt("mode",1),model.fluvHeight, model.fluvNumber, model.fluvWeight, model.speed, model.wideness, preferences.getInt("heightness_", 15), preferences.getInt("dimAlpha_", 125),
                        preferences.getInt("dimHeight_", 100), preferences.getInt("rotation_", 0), preferences.getInt("horizontalOffset_", 0), preferences.getInt("verticalOffset_", 0),
                        preferences.getInt("color_", Color.BLACK), preferences.getInt("colorLeft_", 0xff333333.toInt()), preferences.getInt("colorRight_", 0xff666666.toInt()), widgetSelected,
                        Math.round(preferences.getInt("widgetX", 0) * vProportion), Math.round(preferences.getInt("widgetY", 0) * vProportion), Math.round(preferences.getInt("widgetXsize", 454) * vProportion),
                        style, preferences.getBoolean(SysPypots.SETTINGS_DISCRETO, false), preferences.getBoolean(SysPypots.SETTINGS_HALO, false), color1, color2, color3, color4)
            }

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

        val THEME1="{\"mode\":1,\"color\":-14352498,\"colorLeft\":-12566464,\"colorRight\":-9737365,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":0,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":94,\"fluvNumber\":12,\"fluvWeight\":43,\"halo\":true,\"heightness\":47,\"horizontalOffset\":0,\"rotation\":156,\"speed\":3,\"style\":0,\"verticalOffset\":0,\"wideness\":376,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":176,\"widgetY\":157}"
        val THEME2="{\"mode\":1,\"color\":-8010497,\"colorLeft\":-16744193,\"colorRight\":-1398738,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":52,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":53,\"fluvNumber\":31,\"fluvWeight\":16,\"halo\":true,\"heightness\":69,\"horizontalOffset\":0,\"rotation\":127,\"speed\":3,\"style\":0,\"verticalOffset\":-248,\"wideness\":212,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":175,\"widgetY\":157}"
        val THEME3="{\"mode\":1,\"color\":-1,\"colorLeft\":-1513240,\"colorRight\":-1184275,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":0,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":20,\"fluvNumber\":23,\"fluvWeight\":3,\"halo\":true,\"heightness\":19,\"horizontalOffset\":0,\"rotation\":0,\"speed\":1,\"style\":0,\"verticalOffset\":0,\"wideness\":396,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":175,\"widgetY\":157}"
        val THEME4="{\"mode\":1,\"color\":-14336,\"colorLeft\":-65536,\"colorRight\":-7274348,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":126,\"dimHeight\":147,\"discreto\":true,\"fluvHeight\":46,\"fluvNumber\":36,\"fluvWeight\":36,\"halo\":true,\"heightness\":69,\"horizontalOffset\":0,\"rotation\":90,\"speed\":2,\"style\":0,\"verticalOffset\":0,\"wideness\":578,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":175,\"widgetY\":157}"
        val THEME5="{\"mode\":1,\"color\":-31196,\"colorLeft\":-6710887,\"colorRight\":-9868951,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":0,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":78,\"fluvNumber\":12,\"fluvWeight\":68,\"halo\":true,\"heightness\":39,\"horizontalOffset\":0,\"rotation\":9,\"speed\":3,\"style\":0,\"verticalOffset\":0,\"wideness\":332,\"widgetSelected\":1,\"widgetX\":165,\"widgetXsize\":178,\"widgetY\":163}"
        val THEME6="{\"mode\":1,\"color\":-16777216,\"colorLeft\":-5712199,\"colorRight\":-3495722,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":0,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":27,\"fluvNumber\":56,\"fluvWeight\":8,\"halo\":true,\"heightness\":62,\"horizontalOffset\":0,\"rotation\":0,\"speed\":1,\"style\":0,\"verticalOffset\":0,\"wideness\":369,\"widgetSelected\":1,\"widgetX\":165,\"widgetXsize\":182,\"widgetY\":164}"
        val THEME7="{\"mode\":1,\"color\":-13369590,\"colorLeft\":-16174336,\"colorRight\":-16376064,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":154,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":30,\"fluvNumber\":12,\"fluvWeight\":9,\"halo\":true,\"heightness\":15,\"horizontalOffset\":0,\"rotation\":90,\"speed\":4,\"style\":0,\"verticalOffset\":0,\"wideness\":729,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":170,\"widgetY\":156}"
        val THEME8="{\"mode\":1,\"color\":-1,\"colorLeft\":-14352195,\"colorRight\":-5763584,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":126,\"dimHeight\":147,\"discreto\":true,\"fluvHeight\":22,\"fluvNumber\":75,\"fluvWeight\":8,\"halo\":true,\"heightness\":69,\"horizontalOffset\":0,\"rotation\":0,\"speed\":7,\"style\":0,\"verticalOffset\":-783,\"wideness\":425,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":175,\"widgetY\":157}"
        val THEME9="{\"mode\":1,\"color\":-62811,\"colorLeft\":-9895362,\"colorRight\":-16777216,\"colorW1\":0,\"colorW2\":0,\"colorW3\":0,\"colorW4\":0,\"dimAlpha\":154,\"dimHeight\":500,\"discreto\":true,\"fluvHeight\":18,\"fluvNumber\":93,\"fluvWeight\":8,\"halo\":true,\"heightness\":69,\"horizontalOffset\":0,\"rotation\":41,\"speed\":2,\"style\":0,\"verticalOffset\":0,\"wideness\":114,\"widgetSelected\":1,\"widgetX\":158,\"widgetXsize\":170,\"widgetY\":156}"

        val MODEL1="{\"fluvHeight\":94,\"fluvHeightDef\":80,\"fluvNumber\":12,\"fluvWeight\":43,\"fluvs\":[{\"direction\":1.0,\"max\":245,\"min\":149,\"size\":216.73575},{\"direction\":1.0,\"max\":351,\"min\":91,\"size\":346.7346},{\"direction\":1.0,\"max\":273,\"min\":113,\"size\":257.73572},{\"direction\":1.0,\"max\":384,\"min\":241,\"size\":340.7346},{\"direction\":1.0,\"max\":343,\"min\":229,\"size\":312.73486},{\"direction\":1.0,\"max\":379,\"min\":240,\"size\":328.7346},{\"direction\":-1.0,\"max\":356,\"min\":163,\"size\":202.26457},{\"direction\":1.0,\"max\":377,\"min\":276,\"size\":349.7346},{\"direction\":1.0,\"max\":241,\"min\":138,\"size\":211.73575},{\"direction\":1.0,\"max\":252,\"min\":75,\"size\":113.27504},{\"direction\":-1.0,\"max\":305,\"min\":76,\"size\":252.55515},{\"direction\":1.0,\"max\":303,\"min\":143,\"size\":291.73526}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":502,\"min\":38,\"speed\":3,\"wideness\":376,\"width\":1080}"
        val MODEL2="{\"fluvHeight\":53,\"fluvHeightDef\":80,\"fluvNumber\":31,\"fluvWeight\":16,\"fluvs\":[{\"direction\":1.0,\"max\":201,\"min\":52,\"size\":147.63509},{\"direction\":1.0,\"max\":130,\"min\":23,\"size\":121.63507},{\"direction\":-1.0,\"max\":222,\"min\":119,\"size\":196.45563},{\"direction\":-1.0,\"max\":107,\"min\":21,\"size\":60.514977},{\"direction\":-1.0,\"max\":170,\"min\":10,\"size\":44.364895},{\"direction\":1.0,\"max\":181,\"min\":46,\"size\":131.6351},{\"direction\":-1.0,\"max\":178,\"min\":109,\"size\":147.97537},{\"direction\":-1.0,\"max\":85,\"min\":8,\"size\":53.54493},{\"direction\":-1.0,\"max\":86,\"min\":15,\"size\":56.444927},{\"direction\":-1.0,\"max\":115,\"min\":58,\"size\":87.48497},{\"direction\":1.0,\"max\":188,\"min\":46,\"size\":136.63512},{\"direction\":-1.0,\"max\":154,\"min\":9,\"size\":86.63487},{\"direction\":-1.0,\"max\":137,\"min\":55,\"size\":126.37504},{\"direction\":-1.0,\"max\":157,\"min\":73,\"size\":115.74486},{\"direction\":-1.0,\"max\":121,\"min\":63,\"size\":93.55499},{\"direction\":-1.0,\"max\":89,\"min\":31,\"size\":62.574883},{\"direction\":1.0,\"max\":180,\"min\":48,\"size\":154.63512},{\"direction\":1.0,\"max\":208,\"min\":124,\"size\":180.22534},{\"direction\":-1.0,\"max\":209,\"min\":108,\"size\":135.42462},{\"direction\":-1.0,\"max\":171,\"min\":93,\"size\":155.67557},{\"direction\":-1.0,\"max\":217,\"min\":114,\"size\":215.5156},{\"direction\":-1.0,\"max\":136,\"min\":71,\"size\":78.05488},{\"direction\":1.0,\"max\":117,\"min\":40,\"size\":109.085106},{\"direction\":-1.0,\"max\":197,\"min\":40,\"size\":128.77466},{\"direction\":-1.0,\"max\":210,\"min\":142,\"size\":187.9357},{\"direction\":1.0,\"max\":134,\"min\":38,\"size\":123.63506},{\"direction\":1.0,\"max\":189,\"min\":36,\"size\":150.63507},{\"direction\":-1.0,\"max\":204,\"min\":107,\"size\":183.86569},{\"direction\":-1.0,\"max\":203,\"min\":46,\"size\":166.91519},{\"direction\":1.0,\"max\":77,\"min\":21,\"size\":20.985033},{\"direction\":-1.0,\"max\":141,\"min\":63,\"size\":122.85531}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":538,\"min\":2,\"speed\":3,\"wideness\":212,\"width\":1080}"
        val MODEL3="{\"fluvHeight\":20,\"fluvHeightDef\":80,\"fluvNumber\":23,\"fluvWeight\":3,\"fluvs\":[{\"direction\":1.0,\"max\":194,\"min\":119,\"size\":160.21078},{\"direction\":1.0,\"max\":394,\"min\":359,\"size\":384.28006},{\"direction\":1.0,\"max\":332,\"min\":275,\"size\":330.37332},{\"direction\":-1.0,\"max\":190,\"min\":153,\"size\":155.78949},{\"direction\":-1.0,\"max\":245,\"min\":213,\"size\":216.63977},{\"direction\":-1.0,\"max\":27,\"min\":4,\"size\":20.130007},{\"direction\":1.0,\"max\":334,\"min\":96,\"size\":281.3722},{\"direction\":1.0,\"max\":188,\"min\":3,\"size\":66.36983},{\"direction\":-1.0,\"max\":338,\"min\":304,\"size\":308.6393},{\"direction\":1.0,\"max\":378,\"min\":119,\"size\":370.37332},{\"direction\":-1.0,\"max\":274,\"min\":213,\"size\":251.80016},{\"direction\":1.0,\"max\":384,\"min\":265,\"size\":357.37332},{\"direction\":-1.0,\"max\":97,\"min\":30,\"size\":90.740326},{\"direction\":1.0,\"max\":170,\"min\":144,\"size\":152.11005},{\"direction\":1.0,\"max\":243,\"min\":134,\"size\":151.22989},{\"direction\":1.0,\"max\":393,\"min\":314,\"size\":390.37332},{\"direction\":1.0,\"max\":159,\"min\":48,\"size\":71.11998},{\"direction\":1.0,\"max\":364,\"min\":97,\"size\":276.37186},{\"direction\":1.0,\"max\":110,\"min\":79,\"size\":83.229744},{\"direction\":-1.0,\"max\":63,\"min\":42,\"size\":55.06},{\"direction\":1.0,\"max\":349,\"min\":145,\"size\":288.37256},{\"direction\":-1.0,\"max\":159,\"min\":58,\"size\":91.629234},{\"direction\":-1.0,\"max\":396,\"min\":186,\"size\":235.62743}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":538,\"min\":2,\"speed\":1,\"wideness\":396,\"width\":1080}"
        val MODEL4="{\"fluvHeight\":46,\"fluvHeightDef\":80,\"fluvNumber\":36,\"fluvWeight\":36,\"fluvs\":[{\"direction\":1.0,\"max\":454,\"min\":182,\"size\":185.41953},{\"direction\":-1.0,\"max\":561,\"min\":478,\"size\":516.4382},{\"direction\":1.0,\"max\":461,\"min\":124,\"size\":287.66083},{\"direction\":1.0,\"max\":479,\"min\":393,\"size\":445.66125},{\"direction\":-1.0,\"max\":543,\"min\":184,\"size\":372.33875},{\"direction\":1.0,\"max\":516,\"min\":374,\"size\":381.3587},{\"direction\":-1.0,\"max\":315,\"min\":149,\"size\":212.33931},{\"direction\":-1.0,\"max\":494,\"min\":251,\"size\":489.50134},{\"direction\":1.0,\"max\":378,\"min\":198,\"size\":265.66043},{\"direction\":-1.0,\"max\":283,\"min\":106,\"size\":137.33948},{\"direction\":1.0,\"max\":419,\"min\":71,\"size\":397.66125},{\"direction\":-1.0,\"max\":553,\"min\":396,\"size\":498.33707},{\"direction\":-1.0,\"max\":360,\"min\":56,\"size\":257.33875},{\"direction\":1.0,\"max\":208,\"min\":118,\"size\":182.66052},{\"direction\":1.0,\"max\":203,\"min\":150,\"size\":154.49954},{\"direction\":1.0,\"max\":538,\"min\":457,\"size\":508.66125},{\"direction\":-1.0,\"max\":463,\"min\":409,\"size\":440.5407},{\"direction\":-1.0,\"max\":259,\"min\":86,\"size\":240.60031},{\"direction\":1.0,\"max\":507,\"min\":389,\"size\":475.66125},{\"direction\":1.0,\"max\":428,\"min\":344,\"size\":407.66125},{\"direction\":-1.0,\"max\":570,\"min\":426,\"size\":502.33707},{\"direction\":1.0,\"max\":268,\"min\":112,\"size\":192.66052},{\"direction\":-1.0,\"max\":477,\"min\":416,\"size\":468.8413},{\"direction\":-1.0,\"max\":486,\"min\":401,\"size\":464.48077},{\"direction\":1.0,\"max\":570,\"min\":221,\"size\":244.39984},{\"direction\":1.0,\"max\":352,\"min\":261,\"size\":292.37994},{\"direction\":1.0,\"max\":284,\"min\":146,\"size\":205.66052},{\"direction\":-1.0,\"max\":456,\"min\":281,\"size\":413.57935},{\"direction\":1.0,\"max\":463,\"min\":294,\"size\":393.66125},{\"direction\":1.0,\"max\":450,\"min\":166,\"size\":356.66125},{\"direction\":-1.0,\"max\":298,\"min\":245,\"size\":292.6813},{\"direction\":1.0,\"max\":510,\"min\":303,\"size\":370.66125},{\"direction\":1.0,\"max\":419,\"min\":121,\"size\":181.66052},{\"direction\":-1.0,\"max\":286,\"min\":212,\"size\":247.33952},{\"direction\":1.0,\"max\":532,\"min\":104,\"size\":499.66125},{\"direction\":1.0,\"max\":263,\"min\":157,\"size\":221.66052}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":524,\"min\":16,\"speed\":2,\"wideness\":578,\"width\":1080}"
        val MODEL5="{\"fluvHeight\":78,\"fluvHeightDef\":80,\"fluvNumber\":12,\"fluvWeight\":68,\"fluvs\":[{\"direction\":-1.0,\"max\":182,\"min\":65,\"size\":75.54},{\"direction\":1.0,\"max\":221,\"min\":90,\"size\":169.4598},{\"direction\":-1.0,\"max\":321,\"min\":86,\"size\":179.54034},{\"direction\":1.0,\"max\":363,\"min\":224,\"size\":284.45993},{\"direction\":1.0,\"max\":281,\"min\":42,\"size\":275.45984},{\"direction\":1.0,\"max\":368,\"min\":266,\"size\":324.4603},{\"direction\":1.0,\"max\":246,\"min\":128,\"size\":177.82976},{\"direction\":1.0,\"max\":205,\"min\":107,\"size\":164.45981},{\"direction\":1.0,\"max\":307,\"min\":178,\"size\":248.45966},{\"direction\":1.0,\"max\":198,\"min\":43,\"size\":99.46003},{\"direction\":1.0,\"max\":333,\"min\":195,\"size\":203.84027},{\"direction\":1.0,\"max\":406,\"min\":308,\"size\":378.4603}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":502,\"min\":38,\"speed\":3,\"wideness\":332,\"width\":1080}"
        val MODEL6="{\"fluvHeight\":27,\"fluvHeightDef\":80,\"fluvNumber\":56,\"fluvWeight\":8,\"fluvs\":[{\"direction\":1.0,\"max\":221,\"min\":8,\"size\":129.85532},{\"direction\":1.0,\"max\":92,\"min\":28,\"size\":51.854877},{\"direction\":-1.0,\"max\":124,\"min\":95,\"size\":104.144745},{\"direction\":-1.0,\"max\":88,\"min\":7,\"size\":22.145075},{\"direction\":1.0,\"max\":366,\"min\":299,\"size\":358.85626},{\"direction\":-1.0,\"max\":203,\"min\":86,\"size\":126.14476},{\"direction\":-1.0,\"max\":288,\"min\":220,\"size\":271.23383},{\"direction\":1.0,\"max\":117,\"min\":65,\"size\":108.855255},{\"direction\":1.0,\"max\":254,\"min\":17,\"size\":76.85513},{\"direction\":1.0,\"max\":310,\"min\":76,\"size\":134.85538},{\"direction\":1.0,\"max\":218,\"min\":61,\"size\":195.8553},{\"direction\":1.0,\"max\":300,\"min\":81,\"size\":91.69505},{\"direction\":-1.0,\"max\":307,\"min\":147,\"size\":172.1447},{\"direction\":1.0,\"max\":185,\"min\":33,\"size\":108.855255},{\"direction\":1.0,\"max\":128,\"min\":26,\"size\":122.855255},{\"direction\":-1.0,\"max\":177,\"min\":36,\"size\":37.145123},{\"direction\":1.0,\"max\":364,\"min\":241,\"size\":277.85626},{\"direction\":1.0,\"max\":294,\"min\":252,\"size\":257.6645},{\"direction\":1.0,\"max\":172,\"min\":73,\"size\":169.8553},{\"direction\":1.0,\"max\":207,\"min\":159,\"size\":178.2853},{\"direction\":1.0,\"max\":253,\"min\":150,\"size\":223.8553},{\"direction\":1.0,\"max\":319,\"min\":250,\"size\":297.85626},{\"direction\":-1.0,\"max\":298,\"min\":205,\"size\":258.14374},{\"direction\":1.0,\"max\":199,\"min\":31,\"size\":123.855255},{\"direction\":-1.0,\"max\":328,\"min\":32,\"size\":120.14482},{\"direction\":-1.0,\"max\":190,\"min\":33,\"size\":105.144745},{\"direction\":1.0,\"max\":214,\"min\":119,\"size\":183.8553},{\"direction\":1.0,\"max\":293,\"min\":230,\"size\":269.856},{\"direction\":1.0,\"max\":70,\"min\":30,\"size\":39.605007},{\"direction\":1.0,\"max\":325,\"min\":259,\"size\":323.85626},{\"direction\":-1.0,\"max\":267,\"min\":238,\"size\":254.33447},{\"direction\":-1.0,\"max\":315,\"min\":264,\"size\":311.2058},{\"direction\":1.0,\"max\":260,\"min\":163,\"size\":182.8553},{\"direction\":1.0,\"max\":91,\"min\":25,\"size\":53.854877},{\"direction\":1.0,\"max\":269,\"min\":203,\"size\":262.8555},{\"direction\":1.0,\"max\":291,\"min\":122,\"size\":186.8553},{\"direction\":1.0,\"max\":180,\"min\":59,\"size\":156.8553},{\"direction\":1.0,\"max\":281,\"min\":230,\"size\":243.61528},{\"direction\":1.0,\"max\":297,\"min\":113,\"size\":119.844894},{\"direction\":-1.0,\"max\":262,\"min\":192,\"size\":219.1447},{\"direction\":-1.0,\"max\":325,\"min\":285,\"size\":319.3355},{\"direction\":1.0,\"max\":343,\"min\":303,\"size\":333.85626},{\"direction\":1.0,\"max\":280,\"min\":5,\"size\":212.8553},{\"direction\":1.0,\"max\":303,\"min\":144,\"size\":147.79497},{\"direction\":1.0,\"max\":294,\"min\":72,\"size\":106.855255},{\"direction\":1.0,\"max\":177,\"min\":116,\"size\":176.8553},{\"direction\":1.0,\"max\":118,\"min\":75,\"size\":117.855255},{\"direction\":1.0,\"max\":268,\"min\":220,\"size\":266.85577},{\"direction\":1.0,\"max\":258,\"min\":95,\"size\":217.8553},{\"direction\":-1.0,\"max\":164,\"min\":90,\"size\":163.33522},{\"direction\":1.0,\"max\":236,\"min\":196,\"size\":214.6353},{\"direction\":1.0,\"max\":277,\"min\":227,\"size\":263.85556},{\"direction\":1.0,\"max\":36,\"min\":4,\"size\":23.284979},{\"direction\":-1.0,\"max\":42,\"min\":8,\"size\":24.305063},{\"direction\":-1.0,\"max\":181,\"min\":2,\"size\":32.145123},{\"direction\":1.0,\"max\":158,\"min\":47,\"size\":123.855255}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":538,\"min\":2,\"speed\":1,\"wideness\":369,\"width\":1080}"
        val MODEL7="{\"fluvHeight\":30,\"fluvHeightDef\":80,\"fluvNumber\":12,\"fluvWeight\":9,\"fluvs\":[{\"direction\":-1.0,\"max\":497,\"min\":438,\"size\":463.90027},{\"direction\":-1.0,\"max\":440,\"min\":350,\"size\":360.77966},{\"direction\":-1.0,\"max\":631,\"min\":109,\"size\":627.7816},{\"direction\":1.0,\"max\":714,\"min\":532,\"size\":549.61926},{\"direction\":-1.0,\"max\":633,\"min\":181,\"size\":606.46094},{\"direction\":-1.0,\"max\":652,\"min\":453,\"size\":588.8196},{\"direction\":-1.0,\"max\":220,\"min\":124,\"size\":159.66005},{\"direction\":-1.0,\"max\":313,\"min\":281,\"size\":297.30005},{\"direction\":-1.0,\"max\":520,\"min\":67,\"size\":504.7803},{\"direction\":1.0,\"max\":688,\"min\":207,\"size\":402.66058},{\"direction\":-1.0,\"max\":473,\"min\":388,\"size\":397.85974},{\"direction\":1.0,\"max\":498,\"min\":452,\"size\":479.2201}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":522,\"min\":18,\"speed\":4,\"wideness\":729,\"width\":1080}"
        val MODEL8="{\"fluvHeight\":22,\"fluvHeightDef\":80,\"fluvNumber\":75,\"fluvWeight\":8,\"fluvs\":[{\"direction\":1.0,\"max\":390,\"min\":37,\"size\":145.00008},{\"direction\":-1.0,\"max\":217,\"min\":192,\"size\":217.19995},{\"direction\":1.0,\"max\":404,\"min\":159,\"size\":294.19986},{\"direction\":-1.0,\"max\":271,\"min\":97,\"size\":209.16011},{\"direction\":1.0,\"max\":52,\"min\":23,\"size\":37.559994},{\"direction\":1.0,\"max\":62,\"min\":39,\"size\":41.640003},{\"direction\":1.0,\"max\":402,\"min\":47,\"size\":240.80011},{\"direction\":-1.0,\"max\":347,\"min\":208,\"size\":222.51994},{\"direction\":-1.0,\"max\":377,\"min\":123,\"size\":357.7598},{\"direction\":-1.0,\"max\":238,\"min\":117,\"size\":159.20023},{\"direction\":1.0,\"max\":406,\"min\":159,\"size\":322.19992},{\"direction\":1.0,\"max\":165,\"min\":73,\"size\":103.00004},{\"direction\":-1.0,\"max\":375,\"min\":320,\"size\":368.16016},{\"direction\":1.0,\"max\":424,\"min\":289,\"size\":345.48035},{\"direction\":1.0,\"max\":346,\"min\":306,\"size\":321.59998},{\"direction\":1.0,\"max\":168,\"min\":132,\"size\":139.6399},{\"direction\":-1.0,\"max\":345,\"min\":323,\"size\":332.51996},{\"direction\":-1.0,\"max\":266,\"min\":173,\"size\":229.43994},{\"direction\":-1.0,\"max\":419,\"min\":46,\"size\":247.96053},{\"direction\":1.0,\"max\":304,\"min\":155,\"size\":193.1197},{\"direction\":1.0,\"max\":349,\"min\":256,\"size\":283.5198},{\"direction\":1.0,\"max\":381,\"min\":126,\"size\":156.95978},{\"direction\":1.0,\"max\":325,\"min\":286,\"size\":319.99994},{\"direction\":1.0,\"max\":268,\"min\":139,\"size\":161.35966},{\"direction\":-1.0,\"max\":196,\"min\":69,\"size\":93.720085},{\"direction\":1.0,\"max\":230,\"min\":72,\"size\":114.91977},{\"direction\":-1.0,\"max\":323,\"min\":141,\"size\":263.96027},{\"direction\":1.0,\"max\":265,\"min\":180,\"size\":182.47987},{\"direction\":-1.0,\"max\":409,\"min\":212,\"size\":296.80005},{\"direction\":-1.0,\"max\":238,\"min\":195,\"size\":212.6},{\"direction\":1.0,\"max\":122,\"min\":87,\"size\":101.80001},{\"direction\":-1.0,\"max\":138,\"min\":102,\"size\":123.51999},{\"direction\":1.0,\"max\":226,\"min\":83,\"size\":198.56012},{\"direction\":-1.0,\"max\":331,\"min\":274,\"size\":320.1599},{\"direction\":-1.0,\"max\":288,\"min\":121,\"size\":254.20024},{\"direction\":1.0,\"max\":150,\"min\":28,\"size\":103.519905},{\"direction\":1.0,\"max\":288,\"min\":224,\"size\":287.20007},{\"direction\":1.0,\"max\":214,\"min\":59,\"size\":180.6401},{\"direction\":1.0,\"max\":292,\"min\":85,\"size\":258.04028},{\"direction\":1.0,\"max\":200,\"min\":127,\"size\":168.27994},{\"direction\":1.0,\"max\":354,\"min\":66,\"size\":199.08018},{\"direction\":-1.0,\"max\":195,\"min\":63,\"size\":157.08011},{\"direction\":1.0,\"max\":404,\"min\":72,\"size\":370.9999},{\"direction\":-1.0,\"max\":159,\"min\":52,\"size\":104.520004},{\"direction\":1.0,\"max\":177,\"min\":34,\"size\":78.319885},{\"direction\":1.0,\"max\":367,\"min\":328,\"size\":359.88},{\"direction\":-1.0,\"max\":285,\"min\":149,\"size\":228.4403},{\"direction\":1.0,\"max\":355,\"min\":202,\"size\":254.04012},{\"direction\":1.0,\"max\":268,\"min\":237,\"size\":262.55994},{\"direction\":1.0,\"max\":155,\"min\":125,\"size\":139.88005},{\"direction\":-1.0,\"max\":306,\"min\":141,\"size\":210.96007},{\"direction\":1.0,\"max\":255,\"min\":163,\"size\":171.83997},{\"direction\":-1.0,\"max\":203,\"min\":162,\"size\":182.60004},{\"direction\":1.0,\"max\":163,\"min\":60,\"size\":114.44004},{\"direction\":-1.0,\"max\":177,\"min\":140,\"size\":169.32007},{\"direction\":-1.0,\"max\":407,\"min\":193,\"size\":408.31985},{\"direction\":1.0,\"max\":368,\"min\":93,\"size\":201.99991},{\"direction\":1.0,\"max\":298,\"min\":27,\"size\":214.76022},{\"direction\":1.0,\"max\":327,\"min\":83,\"size\":133.55984},{\"direction\":-1.0,\"max\":386,\"min\":351,\"size\":375.64},{\"direction\":1.0,\"max\":418,\"min\":107,\"size\":264.4002},{\"direction\":1.0,\"max\":106,\"min\":72,\"size\":86.32},{\"direction\":-1.0,\"max\":276,\"min\":234,\"size\":235.03998},{\"direction\":-1.0,\"max\":397,\"min\":71,\"size\":218.44041},{\"direction\":1.0,\"max\":408,\"min\":118,\"size\":247.84004},{\"direction\":-1.0,\"max\":373,\"min\":276,\"size\":329.9203},{\"direction\":1.0,\"max\":407,\"min\":55,\"size\":246.88013},{\"direction\":-1.0,\"max\":217,\"min\":32,\"size\":74.71992},{\"direction\":-1.0,\"max\":252,\"min\":46,\"size\":134.63991},{\"direction\":1.0,\"max\":138,\"min\":101,\"size\":122.24},{\"direction\":1.0,\"max\":392,\"min\":198,\"size\":386.59976},{\"direction\":-1.0,\"max\":228,\"min\":206,\"size\":215.59995},{\"direction\":-1.0,\"max\":400,\"min\":303,\"size\":372.32025},{\"direction\":-1.0,\"max\":415,\"min\":372,\"size\":379.04004},{\"direction\":-1.0,\"max\":130,\"min\":104,\"size\":114.24001}],\"fps\":60,\"height\":2400,\"isWallpaper\":false,\"max\":524,\"min\":16,\"speed\":7,\"wideness\":425,\"width\":1080}"
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

            preferences.edit().putInt("mode", theme.mode).apply()
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