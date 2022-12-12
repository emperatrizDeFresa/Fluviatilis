package emperatriz.fluviatilis.settings

import android.R.attr.button
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.Shader.TileMode
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.view.Window
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.graphics.ColorUtils
import emperatriz.fluviatilis.liveWallpaper.*
import emperatriz.fluviatilis.model.WallpaperModel
import emperatriz.fluviatilis.themes.ThemeManager
import emperatriz.fluviatilis.themes.ThemeManager.companion.getCurrentTheme
import emperatriz.fluviatilis.themes.ThemeManager.companion.getModel
import emperatriz.fluviatilis.themes.ThemeManager.companion.getModelToLoad
import emperatriz.fluviatilis.themes.ThemeManager.companion.getTheme
import emperatriz.fluviatilis.themes.ThemeManager.companion.getThemeToLoad
import emperatriz.fluviatilis.themes.ThemeManager.companion.loadTheme
import emperatriz.fluviatilis.themes.ThemeManager.companion.saveTheme
import emperatriz.fluviatilis.themes.ThemePreview
import emperatriz.fluviatilis.widgets.pypots.SysPypots
import emperatriz.fluviatilis.widgets.spin.SpinDrawUtils
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*


class SettingsActivity : BaseSettingsActivity() {

    var settingsVisible=false;
    var widgetsVisible=false;
    var colorPickerVisible=false;
    var themesVisible=false;

    var lastColors = LinkedList<Int>()


    override val settingsActivityComponent by lazy { ComponentName(this, "$packageName.LauncherSettingsActivity") }
    override val wallpaperServiceComponent by lazy { ComponentName(this, WallpaperService::class.java) }



    private fun getRendererModel(pref:SharedPreferences): WallpaperModel {
            return (settings_live_wallpaper.renderer as WallpaperDrawer).model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val preferences = getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)

        settings_live_wallpaper.renderer = WallpaperDrawer(applicationContext, false)





        themes.setColorFilter(Color.BLACK)



        floatingActionButton.setOnClickListener {
            if (widgetsVisible){
                closeWidgets{openSettings()}
            }
            else if (colorPickerVisible){
                closeColorPicker{openSettings()}
            }
            else if (themesVisible){
                closeThemes{openSettings()}
            }
            else{
                if (settingsVisible){
                    closeSettings{}
                }
                else{
                    openSettings()
                }
            }

        }



        widgetClock.setOnClickListener {
            if (settingsVisible){
                closeSettings{openWidgets()}
            }
            else if (colorPickerVisible){
                closeColorPicker{openWidgets()}
            }
            else if (themesVisible){
                closeThemes{openWidgets()}
            }
            else{
                if (widgetsVisible){
                    closeWidgets{}
                }
                else{
                    openWidgets()
                }
            }
        }

        themes.setOnClickListener {
            if (settingsVisible){
                closeSettings{openThemes()}
            }
            else if (colorPickerVisible){
                closeColorPicker{openThemes()}
            }
            else if (widgetsVisible){
                closeWidgets{openThemes()}
            }
            else{
                if (themesVisible){
                    closeThemes{}
                }
                else{
                    openThemes()
                }
            }
        }
        val dm = DisplayMetrics()

            separatorWidth.max = getRendererModel(preferences).maxFluvWeight()
            separatorWidth.progress = preferences.getInt("fluvWeight", 12)
            separatorWidth.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    getRendererModel(preferences).fluvWeight = Math.max(1, progress)
                    preferences.edit().putInt("fluvWeight", Math.max(1, progress)).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })

            fluvNumber.min = 12
            fluvNumber.max = getRendererModel(preferences).maxFluvNumber()
            fluvNumber.progress = preferences.getInt("fluvNumber", 16)
            fluvNumber.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    getRendererModel(preferences).fluvNumber = Math.max(12, progress)
                    getRendererModel(preferences).fluvHeight = Math.round((getRendererModel(preferences).height * ((preferences.getInt("heightness", 15)).toFloat() / 100)) / Math.max(12, progress)).toInt()
                    wideness.min = getRendererModel(preferences).fluvHeight * 2
                    getRendererModel(preferences).initFluvs()
                    preferences.edit().putInt("fluvNumber", Math.max(12, progress)).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                    separatorWidth.max = getRendererModel(preferences).maxFluvWeight()
                }
            })

            speed.max = getRendererModel(preferences).maxSpeed()
            speed.progress = preferences.getInt("speed", 2)
            speed.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    getRendererModel(preferences).speed = progress
                    getRendererModel(preferences).initFluvs()
                    preferences.edit().putInt("speed", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })

            wideness.min = getRendererModel(preferences).fluvHeight*4

            windowManager.defaultDisplay.getRealMetrics(dm)
            wideness.max =dm.heightPixels/2
            wideness.progress = preferences.getInt("wideness", 380)
            wideness.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    getRendererModel(preferences).wideness = Math.max(1, progress)
                    getRendererModel(preferences).initFluvs()
                    preferences.edit().putInt("wideness", Math.max(1, progress)).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                    preferences.edit().putBoolean("changedWallpaper", true).apply()
                }
            })

            heightness.max = 100
            heightness.progress = preferences.getInt("heightness", 15)
            heightness.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    preferences.edit().putInt("heightness", Math.max(1, progress)).apply()
//                fluvNumber.progress = Math.min(Math.max(12, Math.round((dm.heightPixels * ((progress).toFloat() / 100)) / (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight)),
//                        (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvNumber())
                    getRendererModel(preferences).fluvHeight = Math.round((getRendererModel(preferences).height * ((Math.max(1, progress)).toFloat() / 100)) / fluvNumber.progress).toInt()
                    wideness.min = getRendererModel(preferences).fluvHeight * 2
                    getRendererModel(preferences).initFluvs()

                    separatorWidth.max = getRendererModel(preferences).maxFluvWeight()
                    preferences.edit().putBoolean("changed", true).apply()
                    preferences.edit().putBoolean("changedWallpaper", true).apply()

                }
            })

            dimAlpha.max = 255
            dimAlpha.progress = preferences.getInt("dimAlpha", 125)
            dimAlpha.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    preferences.edit().putInt("dimAlpha", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })

            dimHeight.max = 500
            dimHeight.progress = preferences.getInt("dimHeight", 100)
            dimHeight.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    preferences.edit().putInt("dimHeight", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })
            rotation.max = 180
            rotation.progress = preferences.getInt("rotation", 0)
            rotation.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (progress % 45 == 0) {
                        vibraFuerte()
                    } else {
                        vibra(2)
                    }
                    preferences.edit().putInt("rotation", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })

            horizontalOffset.max = dm.widthPixels
            horizontalOffset.progress = preferences.getInt("horizontalOffset", 0)+dm.widthPixels / 2
            horizontalOffset.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress_: Int, fromUser: Boolean) {
                    val progress = progress_ - dm.widthPixels / 2
                    if (progress == 0) {
                        vibraFuerte()
                    } else {
                        vibra(2)
                    }
                    preferences.edit().putInt("horizontalOffset", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })

            verticalOffset.max = dm.heightPixels
            verticalOffset.progress = preferences.getInt("verticalOffset", 0)+ dm.heightPixels / 2
            verticalOffset.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress_: Int, fromUser: Boolean) {
                    val progress = progress_ - dm.heightPixels / 2
                    if (progress == 0) {
                        vibraFuerte()
                    } else {
                        vibra(2)
                    }
                    preferences.edit().putInt("verticalOffset", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })


            colorMiddle.background.setColorFilter(preferences.getInt("color", Color.BLACK), PorterDuff.Mode.SRC_IN)
            colorMiddle.setColorFilter(if (isBrightColor(preferences.getInt("color", Color.BLACK))) Color.BLACK else Color.WHITE)
            colorMiddle.setOnClickListener {
                setColorFor(preferences, "color", it!! as ImageButton)
            }

            colorLeft.background.setColorFilter(preferences.getInt("colorLeft", Color.rgb(50, 50, 50)), PorterDuff.Mode.SRC_IN)
            colorLeft.setColorFilter(if (isBrightColor(preferences.getInt("colorLeft", Color.rgb(50, 50, 50)))) Color.BLACK else Color.WHITE)
            colorLeft.setOnClickListener {
                setColorFor(preferences, "colorLeft", it!! as ImageButton)
            }

            colorRight.background.setColorFilter(preferences.getInt("colorRight", Color.rgb(100, 100, 100)), PorterDuff.Mode.SRC_IN)
            colorRight.setColorFilter(if (isBrightColor(preferences.getInt("colorRight", Color.rgb(100, 100, 100)))) Color.BLACK else Color.WHITE)
            colorRight.setOnClickListener {
                setColorFor(preferences, "colorRight", it!! as ImageButton)
            }



            fluvNumber_.min = 1
            fluvNumber_.max = getRendererModel(preferences).maxBarSize()
            fluvNumber_.progress = preferences.getInt("fluvNumber_", 20)
            fluvNumber_.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2_)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2_)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    getRendererModel(preferences).fluvHeight = progress
                    getRendererModel(preferences).initBars(preferences.getInt("colorLeft_", Color.rgb(50, 50, 50)),
                                                           preferences.getInt("color_", Color.rgb(50, 50, 50)),
                                                           preferences.getInt("colorRight_", Color.rgb(50, 50, 50)))
                    preferences.edit().putInt("fluvNumber_", Math.max(1, progress)).apply()
                    preferences.edit().putBoolean("changed", true).apply()

                }
            })

            speed_.max = 11
            speed_.min = 1
            speed_.progress = preferences.getInt("speed_", 2)
            speed_.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2_)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2_)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    getRendererModel(preferences).speed = progress
                    preferences.edit().putInt("speed_", progress).apply()
                    getRendererModel(preferences).initBars(preferences.getInt("colorLeft_", Color.rgb(50, 50, 50)),
                            preferences.getInt("color_", Color.rgb(50, 50, 50)),
                            preferences.getInt("colorRight_", Color.rgb(50, 50, 50)))
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })



            dimAlpha_.max = 255
            dimAlpha_.progress = preferences.getInt("dimAlpha_", 125)
            dimAlpha_.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2_)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2_)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    preferences.edit().putInt("dimAlpha_", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })

            dimHeight_.max = 500
            dimHeight_.progress = preferences.getInt("dimHeight_", 100)
            dimHeight_.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2_)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2_)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vibra(2)
                    preferences.edit().putInt("dimHeight_", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })
            rotation_.max = 180
            rotation_.progress = preferences.getInt("rotation_", 0)
            rotation_.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    showAll(settingsPanel, settingsPanel2_)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    showOnlyMe(seekBar, settingsPanel, settingsPanel2_)
                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (progress % 45 == 0) {
                        vibraFuerte()
                    } else {
                        vibra(2)
                    }
                    preferences.edit().putInt("rotation_", progress).apply()
                    preferences.edit().putBoolean("changed", true).apply()
                }
            })





            colorMiddle_.background.setColorFilter(preferences.getInt("color_", Color.BLACK), PorterDuff.Mode.SRC_IN)
            colorMiddle_.setColorFilter(if (isBrightColor(preferences.getInt("color_", Color.BLACK))) Color.BLACK else Color.WHITE)
            colorMiddle_.setOnClickListener {
                setColorFor(preferences, "color_", it!! as ImageButton)
            }

            colorLeft_.background.setColorFilter(preferences.getInt("colorLeft_", Color.rgb(50, 50, 50)), PorterDuff.Mode.SRC_IN)
            colorLeft_.setColorFilter(if (isBrightColor(preferences.getInt("colorLeft_", Color.rgb(50, 50, 50)))) Color.BLACK else Color.WHITE)
            colorLeft_.setOnClickListener {
                setColorFor(preferences, "colorLeft_", it!! as ImageButton)
            }

            colorRight_.background.setColorFilter(preferences.getInt("colorRight_", Color.rgb(100, 100, 100)), PorterDuff.Mode.SRC_IN)
            colorRight_.setColorFilter(if (isBrightColor(preferences.getInt("colorRight_", Color.rgb(100, 100, 100)))) Color.BLACK else Color.WHITE)
            colorRight_.setOnClickListener {
                setColorFor(preferences, "colorRight_", it!! as ImageButton)
            }





        if (preferences.getBoolean("showWidget", false)){
            widgetClock.visibility = VISIBLE
        }
        else{
            widgetClock.visibility = GONE
        }

        showWidgetLL.setOnClickListener{
            showWidget.performClick()
        }

        showWidgetLL_.setOnClickListener{
            showWidget.performClick()
        }

        showWidget_.setOnClickListener{
            showWidget.performClick()
        }

        (showWidget as CheckBox).isChecked = preferences.getBoolean("showWidget", false)
        (showWidget_ as CheckBox).isChecked = preferences.getBoolean("showWidget", false)

        showWidget.setOnClickListener{
            preferences.edit().putBoolean("showWidget", (it as CheckBox).isChecked).apply()
            if ((it as CheckBox).isChecked){
                widgetClock.visibility = VISIBLE
            }
            else{
                widgetClock.visibility = GONE
            }
        }


        widgetX.max = dm.widthPixels
        widgetX.progress = preferences.getInt("widgetX", 0)//+preferences.getInt("widgetXsize", 454)/2
        widgetX.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == dm.widthPixels / 2) {
                    vibraFuerte()
                } else {
                    vibra(2)
                }
                widgetX2.progress = progress
                widgetX3.progress = progress
                preferences.edit().putInt("widgetX", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetY.max = dm.heightPixels
        widgetY.progress = preferences.getInt("widgetY", 0)//+preferences.getInt("widgetXsize", 454)/2
        widgetY.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == dm.heightPixels / 2) {
                    vibraFuerte()
                } else {
                    vibra(2)
                }
                widgetY2.progress = progress
                widgetY3.progress = progress
                preferences.edit().putInt("widgetY", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetXsize.max = dm.widthPixels
        widgetXsize.progress = preferences.getInt("widgetXsize", 454)
        widgetXsize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vibra(2)
                widgetXsize2.progress = progress
                widgetXsize3.progress = progress
                val diff = Math.round((preferences.getInt("widgetXsize", 454) - progress) / 2f)


                preferences.edit().putInt("widgetX", widgetX.progress + diff).apply()
                preferences.edit().putInt("widgetY", widgetY.progress + diff).apply()

                preferences.edit().putInt("widgetXsize", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        style.max = 7
        style.progress = preferences.getInt(SysPypots.SETTINGS_DIVISIONES, 1)
        style.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, pypotsSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vibra(2)
                SysPypots.save(SysPypots.SETTINGS_DIVISIONES, if (progress == 7) 9 else progress + 1, this@SettingsActivity)
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()

            }
        })

        complicationsLL.setOnClickListener{
            complications.performClick()
        }

        haloLL.setOnClickListener{
            halo.performClick()
        }

        (complications as CheckBox).isChecked = preferences.getBoolean(SysPypots.SETTINGS_DISCRETO, false)
        complications.setOnClickListener{
            preferences.edit().putBoolean(SysPypots.SETTINGS_DISCRETO, (it as CheckBox).isChecked).apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
            preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
        }

        (halo as CheckBox).isChecked = preferences.getBoolean(SysPypots.SETTINGS_HALO, false)
        halo.setOnClickListener{
            preferences.edit().putBoolean(SysPypots.SETTINGS_HALO, (it as CheckBox).isChecked).apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
            preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
        }



        widgetX2.max = dm.widthPixels
        widgetX2.progress = preferences.getInt("widgetX", 0)//+preferences.getInt("widgetXsize", 454)/2
        widgetX2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == dm.widthPixels / 2) {
                    vibraFuerte()
                } else {
                    vibra(2)
                }
                widgetX.progress = progress
                widgetX3.progress = progress
                preferences.edit().putInt("widgetX", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetY2.max = dm.heightPixels
        widgetY2.progress = preferences.getInt("widgetY", 0)//+preferences.getInt("widgetXsize", 454)/2
        widgetY2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == dm.heightPixels / 2) {
                    vibraFuerte()
                } else {
                    vibra(2)
                }
                widgetY.progress = progress
                widgetY3.progress = progress
                preferences.edit().putInt("widgetY", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })



        widgetXsize2.max = dm.widthPixels
        widgetXsize2.progress = preferences.getInt("widgetXsize", 454)
        widgetXsize2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vibra(2)
                widgetXsize.progress = progress
                widgetXsize3.progress = progress
                val diff = Math.round((preferences.getInt("widgetXsize", 454) - progress) / 2f)


                preferences.edit().putInt("widgetX", widgetX2.progress + diff).apply()
                preferences.edit().putInt("widgetY", widgetY2.progress + diff).apply()

                preferences.edit().putInt("widgetXsize", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })


        style2.max = 5
        style2.progress = preferences.getInt("outmode", 1)
        style2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vibra(2)
                preferences.edit().putInt("outmode", progress).apply()

            }
        })

        infoLL.setOnClickListener{
            info.performClick()
        }

        (info as CheckBox).isChecked = preferences.getBoolean("customInfo", false)
        info.setOnClickListener{
            preferences.edit().putBoolean("customInfo", (it as CheckBox).isChecked).apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
            preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
        }

        colorSpin.background.setColorFilter(preferences.getInt(SpinDrawUtils.ACCENT_COLOR, Color.BLACK), PorterDuff.Mode.SRC_IN)
        colorSpin.setColorFilter(if (isBrightColor(preferences.getInt(SpinDrawUtils.ACCENT_COLOR, Color.BLACK))) Color.BLACK else Color.WHITE)
        colorSpin.setOnClickListener { setColorFor(preferences, SpinDrawUtils.ACCENT_COLOR, it!! as ImageButton) }



        widgetX3.max = dm.widthPixels
        widgetX3.progress = preferences.getInt("widgetX", 0)
        widgetX3.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, customSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, customSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == dm.widthPixels / 2) {
                    vibraFuerte()
                } else {
                    vibra(2)
                }
                widgetX2.progress = progress
                widgetX.progress = progress
                preferences.edit().putInt("widgetX", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetY3.max = dm.heightPixels
        widgetY3.progress = preferences.getInt("widgetY", 0)
        widgetY3.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, customSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, customSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == dm.heightPixels / 2) {
                    vibraFuerte()
                } else {
                    vibra(2)
                }
                widgetY2.progress = progress
                widgetY.progress = progress
                preferences.edit().putInt("widgetY", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetXsize3.max = dm.widthPixels
        widgetXsize3.progress = preferences.getInt("widgetXsize", 454)
        widgetXsize3.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, customSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, customSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vibra(2)
                widgetXsize2.progress = progress
                widgetXsize.progress = progress
                val diff = Math.round((preferences.getInt("widgetXsize", 454) - progress) / 2f)


                preferences.edit().putInt("widgetX", widgetX3.progress + diff).apply()
                preferences.edit().putInt("widgetY", widgetY3.progress + diff).apply()

                preferences.edit().putInt("widgetXsize", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        customAgujas.background.setColorFilter(preferences.getInt("colorAgujas", Color.BLACK), PorterDuff.Mode.SRC_IN)
        customAgujas.setColorFilter(if (isBrightColor(preferences.getInt("colorAgujas", Color.BLACK))) Color.BLACK else Color.WHITE)
        customAgujas.setOnClickListener { setColorFor(preferences, "colorAgujas", it!! as ImageButton) }

        customDial.background.setColorFilter(preferences.getInt("colorEsfera", Color.WHITE), PorterDuff.Mode.SRC_IN)
        customDial.setColorFilter(if (isBrightColor(preferences.getInt("colorEsfera", Color.WHITE))) Color.BLACK else Color.WHITE)
        customDial.setOnClickListener { setColorFor(preferences, "colorEsfera", it!! as ImageButton) }

        customMarcadores.background.setColorFilter(preferences.getInt("colorBorde", Color.DKGRAY), PorterDuff.Mode.SRC_IN)
        customMarcadores.setColorFilter(if (isBrightColor(preferences.getInt("colorBorde", Color.DKGRAY))) Color.BLACK else Color.WHITE)
        customMarcadores.setOnClickListener { setColorFor(preferences, "colorBorde", it!! as ImageButton) }

        customSegundero.background.setColorFilter(preferences.getInt("colorSegundo", Color.RED), PorterDuff.Mode.SRC_IN)
        customSegundero.setColorFilter(if (isBrightColor(preferences.getInt("colorSegundo", Color.RED))) Color.BLACK else Color.WHITE)
        customSegundero.setOnClickListener { setColorFor(preferences, "colorSegundo", it!! as ImageButton) }

        customInfo.background.setColorFilter(preferences.getInt("colorInfo", Color.LTGRAY), PorterDuff.Mode.SRC_IN)
        customInfo.setColorFilter(if (isBrightColor(preferences.getInt("colorInfo", Color.LTGRAY))) Color.BLACK else Color.WHITE)
        customInfo.setOnClickListener { setColorFor(preferences, "colorInfo", it!! as ImageButton) }


        initModeSelector()

        initWidgetSelector()

        initThemes()
    }


    private fun closeWidgets(lambda: () -> Unit){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.VISIBLE
        colorPicker.visibility = GONE
        themesPanel.visibility = GONE
        val anim = ObjectAnimator.ofFloat(widgetsPanel, "alpha", 0f)
        anim.duration = 300
        anim.start()
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                widgetsPanel.setVisibility(View.INVISIBLE)
                lambda()
            }
        })
        widgetsVisible=false
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    private fun closeSettings(lambda: () -> Unit){
        settingsPanel.visibility = View.VISIBLE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = GONE
        themesPanel.visibility = GONE
        val anim = ObjectAnimator.ofFloat(settingsPanel, "alpha", 0f)
        anim.duration = 300
        anim.start()
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                settingsPanel.setVisibility(View.INVISIBLE)
                lambda()
            }
        })
        settingsVisible=false
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    private fun closeColorPicker(lambda: () -> Unit){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = INVISIBLE
        themesPanel.visibility = GONE

        colorPickerVisible=false
        lambda()
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    private fun closeThemes(lambda: () -> Unit){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = GONE
        themesPanel.visibility = INVISIBLE

//        val anim = ObjectAnimator.ofFloat(settingsPanel, "alpha", 0f)
//        anim.duration = 300
//        anim.start()
//        anim.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                themesPanel.setVisibility(View.INVISIBLE)
//                lambda()
//            }
//        })

        themesPanel.alpha = 0f
        lambda()
        themesVisible=false
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    private fun openWidgets(){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.VISIBLE
        colorPicker.visibility = GONE
        themesPanel.visibility = GONE
        val anim = ObjectAnimator.ofFloat(widgetsPanel, "alpha", 1f)
        anim.duration = 300
        anim.start()
        widgetsVisible=true
        val shape = ShapeDrawable(RectShape())
        initWidgetSelector()
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    private fun openSettings(){
        settingsPanel.visibility = View.VISIBLE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = GONE
        themesPanel.visibility = GONE
        val anim = ObjectAnimator.ofFloat(settingsPanel, "alpha", 1f)
        anim.duration = 300
        anim.start()
        settingsVisible=true
        initModeSelector()
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    private fun openColorPicker(){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = VISIBLE
        themesPanel.visibility = GONE
        val anim = ObjectAnimator.ofFloat(colorPicker, "alpha", 1f)
        anim.duration = 300
        anim.start()
        colorPickerVisible=true
        setLastColors()
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    private fun openThemes(){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = GONE
        themesPanel.visibility = VISIBLE
        val anim = ObjectAnimator.ofFloat(themesPanel, "alpha", 1f)
        anim.duration = 300
        anim.start()
        themesVisible=true
        settings_live_wallpaper.canDragWidget = !settingsVisible && !widgetsVisible && !colorPickerVisible && !themesVisible
    }

    override fun onResume() {
        super.onResume()
    }





    fun ImageView.toGrayscale(){
        val matrix = ColorMatrix().apply {
            setSaturation(0f)
            alpha=0.4f
        }
        colorFilter = ColorMatrixColorFilter(matrix)
    }

    fun ImageView.toColor(){
        val matrix = ColorMatrix().apply {
            setSaturation(1f)
            alpha=1f
        }
        colorFilter = ColorMatrixColorFilter(matrix)
    }


    private fun initModeSelector() {

        val preferences = getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
//        pypots.toGrayscale()
//        spin.toGrayscale()
//        custom.toGrayscale()

        when(preferences.getInt("mode", 1)){
            1 -> {
                mode1.toColor()
                mode2.toGrayscale()
                scrollMode1.visibility = VISIBLE
                scrollMode2.visibility = GONE
            }
            2 -> {
                mode1.toGrayscale()
                mode2.toColor()
                scrollMode1.visibility = GONE
                scrollMode2.visibility = VISIBLE
            }
        }

        mode1.setOnClickListener {
            mode1.toColor()
            mode2.toGrayscale()
            scrollMode1.visibility = VISIBLE
            scrollMode2.visibility = GONE
            preferences.edit().putInt("mode", 1).apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
            preferences.edit().putBoolean("changed", true).apply()
            preferences.edit().putBoolean("changedWallpaper", true).apply()
        }

        mode2.setOnClickListener {
            mode1.toGrayscale()
            mode2.toColor()
            scrollMode1.visibility = GONE
            scrollMode2.visibility = VISIBLE
            preferences.edit().putInt("mode", 2).apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
            preferences.edit().putBoolean("changed", true).apply()
            preferences.edit().putBoolean("changedWallpaper", true).apply()
        }

    }


    private fun initWidgetSelector() {

        val preferences = getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
//        pypots.toGrayscale()
//        spin.toGrayscale()
//        custom.toGrayscale()

        when(preferences.getString("selectedWidget", "pypots")){
            "pypots" -> {
                pypots.toColor()
                spin.toGrayscale()
                custom.toGrayscale()
                widgetList.smoothScrollTo(0, 0)
                pypotsSettings.visibility = VISIBLE
                spinSettings.visibility = GONE
                customSettings.visibility = GONE
            }
            "spin" -> {
                pypots.toGrayscale()
                spin.toColor()
                custom.toGrayscale()
                widgetList.smoothScrollTo(150.dp(), 0)
                pypotsSettings.visibility = GONE
                spinSettings.visibility = VISIBLE
                customSettings.visibility = GONE
            }
            "custom" -> {
                pypots.toGrayscale()
                spin.toGrayscale()
                custom.toColor()
                widgetList.smoothScrollTo(3000.dp(), 0)
                pypotsSettings.visibility = GONE
                spinSettings.visibility = GONE
                customSettings.visibility = VISIBLE
            }
        }

        pypots.setOnLongClickListener{
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(dm)
            preferences.edit().putInt("widgetX", dm.widthPixels / 2)
            preferences.edit().putInt("widgetY", dm.heightPixels / 2)

            widgetX.progress = dm.widthPixels/2
            widgetX2.progress = dm.widthPixels/2
            widgetX3.progress = dm.widthPixels/2

            widgetY.progress = dm.heightPixels/2
            widgetY2.progress = dm.heightPixels/2
            widgetY3.progress = dm.heightPixels/2

            vibra(5)

            false
        }

        spin.setOnLongClickListener{
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(dm)
            preferences.edit().putInt("widgetX", dm.widthPixels / 2)
            preferences.edit().putInt("widgetY", dm.heightPixels / 2)

            widgetX.progress = dm.widthPixels/2
            widgetX2.progress = dm.widthPixels/2
            widgetX3.progress = dm.widthPixels/2

            widgetY.progress = dm.heightPixels/2
            widgetY2.progress = dm.heightPixels/2
            widgetY3.progress = dm.heightPixels/2

            vibra(5)

            false
        }

        custom.setOnLongClickListener{
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(dm)
            preferences.edit().putInt("widgetX", dm.widthPixels / 2)
            preferences.edit().putInt("widgetY", dm.heightPixels / 2)

            widgetX.progress = dm.widthPixels/2
            widgetX2.progress = dm.widthPixels/2
            widgetX3.progress = dm.widthPixels/2

            widgetY.progress = dm.heightPixels/2
            widgetY2.progress = dm.heightPixels/2
            widgetY3.progress = dm.heightPixels/2

            vibra(5)

            false
        }



        pypots.setOnClickListener {
            pypots.toColor()
            spin.toGrayscale()
            custom.toGrayscale()
            widgetList.smoothScrollTo(0, 0)
            pypotsSettings.visibility= VISIBLE
            spinSettings.visibility = GONE
            customSettings.visibility = GONE
            preferences.edit().putString("selectedWidget", "pypots").apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
        }

        spin.setOnClickListener {
            pypots.toGrayscale()
            spin.toColor()
            custom.toGrayscale()
            widgetList.smoothScrollTo(150.dp(), 0)
            pypotsSettings.visibility= GONE
            spinSettings.visibility = VISIBLE
            customSettings.visibility = GONE
            preferences.edit().putString("selectedWidget", "spin").apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
        }

        custom.setOnClickListener {
            pypots.toGrayscale()
            spin.toGrayscale()
            custom.toColor()
            widgetList.smoothScrollTo(3000.dp(), 0)
            pypotsSettings.visibility= GONE
            spinSettings.visibility = GONE
            customSettings.visibility = VISIBLE
            preferences.edit().putString("selectedWidget", "custom").apply()
            preferences.edit().putBoolean("changedWidget", true).apply()
        }
        val themeButtons = listOf<LinearLayout>(themeLL1,themeLL2,themeLL3,themeLL4,themeLL5,themeLL6,themeLL7,themeLL8,themeLL9)
        for (position in 1..9){
            themeButtons[position-1].setOnClickListener {th ->
                val popupMenu = PopupMenu(this, th)
                popupMenu.menuInflater.inflate(R.menu.theme_popup, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener{

                    when (it.itemId){
                        R.id.themeSave -> {
                            val currentTheme = getCurrentTheme(this@SettingsActivity,(settings_live_wallpaper.renderer as WallpaperDrawer).model,llHeight,llWidth)
                            saveTheme(this@SettingsActivity,position, currentTheme, (settings_live_wallpaper.renderer as WallpaperDrawer).model)
                            (th as LinearLayout).removeAllViews()
                            th.addView(ThemePreview(this@SettingsActivity, null, currentTheme,(settings_live_wallpaper.renderer as WallpaperDrawer).model.copy(), llHeight, llWidth))
                        }

                        R.id.themeLoad -> {
                            val themeToLoad = getThemeToLoad(this@SettingsActivity, position, llHeight, llWidth)
                            themeToLoad?.let { ttl ->
                                loadTheme(this@SettingsActivity, ttl)
                                (settings_live_wallpaper.renderer as WallpaperDrawer).model = getModelToLoad(this@SettingsActivity, position)!!
                                (settings_live_wallpaper.renderer as WallpaperDrawer).model.initFluvs()
//                                wideness.min = (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight*4
//                                closeThemes{}
                            }
                            val themeToLoad2 = getThemeToLoad(this@SettingsActivity, position, llHeight, llWidth)
                            themeToLoad2?.let { ttl ->
                                loadTheme(this@SettingsActivity, ttl)
                                (settings_live_wallpaper.renderer as WallpaperDrawer).model = getModelToLoad(this@SettingsActivity, position)!!
                                (settings_live_wallpaper.renderer as WallpaperDrawer).model.initFluvs()

                                separatorWidth.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvWeight()
                                separatorWidth.progress = ttl.fluvWeight
                                fluvNumber.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvNumber()
                                fluvNumber.progress = ttl.fluvNumber
                                speed.progress = ttl.speed
                                heightness.progress = ttl.heightness
                                wideness.min = (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight*4
                                wideness.progress = ttl.wideness
                                dimAlpha.progress = ttl.dimAlpha
                                dimHeight.progress = ttl.dimHeight
                                rotation.progress = ttl.rotation
                                horizontalOffset.progress = preferences.getInt("horizontalOffset", 0)+dm.widthPixels / 2
                                verticalOffset.progress = preferences.getInt("verticalOffset", 0)+ dm.heightPixels / 2
                                colorMiddle.background.setColorFilter(ttl.color, PorterDuff.Mode.SRC_IN)
                                colorMiddle.setColorFilter(if (isBrightColor(ttl.color)) Color.BLACK else Color.WHITE)
                                colorLeft.background.setColorFilter(ttl.colorLeft, PorterDuff.Mode.SRC_IN)
                                colorLeft.setColorFilter(if (isBrightColor(ttl.colorLeft)) Color.BLACK else Color.WHITE)
                                colorRight.background.setColorFilter(ttl.colorRight, PorterDuff.Mode.SRC_IN)
                                colorRight.setColorFilter(if (isBrightColor(ttl.colorRight)) Color.BLACK else Color.WHITE)

                                closeThemes{}
                            }
                        }
                    }
                    false
                }
                popupMenu.show()
            }
        }
    }

    var llWidth: Int = 0
    var llHeight: Int = 0

    private fun initThemes(){
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)

        val rectangle = Rect()
        val window: Window = window
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle)
        llWidth = (dm.widthPixels-56.dp()-20.dp())/3
        llHeight = (rectangle.bottom-186.dp()-20.dp())/3

        val themeButtons = listOf<LinearLayout>(themeLL1,themeLL2,themeLL3,themeLL4,themeLL5,themeLL6,themeLL7,themeLL8,themeLL9)
        for (position in 1..9) {
            getTheme(this, position, llHeight, llWidth)?.let {
                val model = getModel(this@SettingsActivity, position)
                model?.let { m->
                    val preview = ThemePreview(this, null, it, m, llHeight, llWidth)
                    themeButtons.get(position-1).addView(preview)
                }
            }
        }
    }

    private fun Int.dp(): Int {
        return (this / resources.displayMetrics.density).toInt()
    }

    fun isBrightColor(color: Int): Boolean {
        if (android.R.color.transparent == color) return true
        var rtnValue = false
        val rgb = intArrayOf(Color.red(color), Color.green(color), Color.blue(color))
        val brightness = Math.sqrt(rgb[0] * rgb[0] * .241 + (rgb[1]
                * rgb[1] * .691) + rgb[2] * rgb[2] * .068).toInt()

        // color is light
        if (brightness >= 200) {
            rtnValue = true
        }
        return rtnValue
    }

    fun showOnlyMe(view: View?, panel1: LinearLayout, panel2: LinearLayout){
        panel1.background = getDrawable(R.drawable.transparent)
        modeList.visibility = INVISIBLE
        for (i in 0 until panel2.childCount){
            val child = panel2.getChildAt(i)
            if (child!=null){
                if(view==null || view.id!=child.id){
                    child.visibility=View.INVISIBLE
                }
            }
        }
    }

    fun showAll(panel1: LinearLayout, panel2: LinearLayout){
        panel1.background = getDrawable(R.drawable.settings_panel)
        modeList.visibility = VISIBLE
        for (i in 0 until panel2.childCount){
            panel2.getChildAt(i)?.visibility=View.VISIBLE
        }
    }



    fun setColorFor(preferences: SharedPreferences, preferencesKey: String, view: ImageButton){


        cpColor.setOnSeekBarChangeListener(null)
        cpSaturacion.setOnSeekBarChangeListener(null)
        cpLuminosidad.setOnSeekBarChangeListener(null)

        var currentColor = Color.valueOf(preferences.getInt(preferencesKey, Color.WHITE))

        val cpGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                0.0f, intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED),
                null, TileMode.CLAMP)

        var colorComponents = FloatArray(3)
        ColorUtils.colorToHSL(currentColor.toArgb(), colorComponents)
        var matiz = colorComponents.get(0)
        var saturacion = colorComponents.get(1)
        var luminosidad = colorComponents.get(2)

        val maxSat = getHSLColor(matiz, 1f, 0.5f)
        val actualSat = getHSLColor(matiz, saturacion, 0.5f)
        val satGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                80.0f, intArrayOf(Color.GRAY, maxSat),
                null, TileMode.CLAMP)
        val lumGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                40.0f, intArrayOf(Color.BLACK, actualSat, Color.WHITE),
                null, TileMode.CLAMP)


        val shape = ShapeDrawable(RectShape())
        shape.paint.shader = cpGradient
        cpColor.progressDrawable = shape

        val shapeSat = ShapeDrawable(RectShape())
        shapeSat.paint.shader = satGradient
        cpSaturacion.progressDrawable = shapeSat

        val shapeLum = ShapeDrawable(RectShape())
        shapeLum.paint.shader = lumGradient
        cpLuminosidad.progressDrawable = shapeLum








        cpSaturacion.progress = Math.round(saturacion * 100)
        cpSaturacion.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(colorPicker2, colorPicker2)
                setLastColors()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, colorPicker2, colorPicker2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                vibra(2)

                saturacion = progress / 100f
                preferences.edit().putInt(preferencesKey, getHSLColor(matiz, saturacion, luminosidad)).apply()
                if (preferences.getInt("mode",1)!=1){
                    getRendererModel(preferences).initBars(preferences.getInt("colorLeft_", Color.rgb(50, 50, 50)),
                            preferences.getInt("color_", Color.rgb(50, 50, 50)),
                            preferences.getInt("colorRight_", Color.rgb(50, 50, 50)))
                    preferences.edit().putBoolean("changed", true).apply()
                    preferences.edit().putBoolean("changedWallpaper", true).apply()
                }

                val lumGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                        0.0f, intArrayOf(Color.BLACK, getHSLColor(matiz, saturacion, 0.5f), Color.WHITE),
                        null, TileMode.CLAMP)
                val shapeLum = ShapeDrawable(RectShape())
                shapeLum.paint.shader = lumGradient
                cpLuminosidad.progressDrawable = shapeLum

                view?.let {
                    it.background.setColorFilter(getHSLColor(matiz, saturacion, luminosidad), PorterDuff.Mode.SRC_IN)
                    it.setColorFilter(if (isBrightColor(getHSLColor(matiz, saturacion, luminosidad))) Color.BLACK else Color.WHITE)
                }
            }
        })

        cpLuminosidad.progress = Math.round(luminosidad * 100)
        cpLuminosidad.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(colorPicker2, colorPicker2)
                setLastColors()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, colorPicker2, colorPicker2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == 50) {
                    vibraFuerte()
                } else {
                    vibra(2)
                }
                luminosidad = progress / 100f
                preferences.edit().putInt(preferencesKey, getHSLColor(matiz, saturacion, luminosidad)).apply()
                if (preferences.getInt("mode",1)!=1){
                    getRendererModel(preferences).initBars(preferences.getInt("colorLeft_", Color.rgb(50, 50, 50)),
                            preferences.getInt("color_", Color.rgb(50, 50, 50)),
                            preferences.getInt("colorRight_", Color.rgb(50, 50, 50)))
                    preferences.edit().putBoolean("changed", true).apply()
                    preferences.edit().putBoolean("changedWallpaper", true).apply()
                }



                view?.let {
                    it.background.setColorFilter(getHSLColor(matiz, saturacion, luminosidad), PorterDuff.Mode.SRC_IN)
                    it.setColorFilter(if (isBrightColor(getHSLColor(matiz, saturacion, luminosidad))) Color.BLACK else Color.WHITE)
                }
            }
        })

        cpColor.progress = Math.round(matiz)
        cpColor.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(colorPicker2, colorPicker2)
                setLastColors()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, colorPicker2, colorPicker2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vibra(2)
                matiz = progress.toFloat()
                preferences.edit().putInt(preferencesKey, getHSLColor(matiz, saturacion, luminosidad)).apply()
                if (preferences.getInt("mode",1)!=1){
                    getRendererModel(preferences).initBars(preferences.getInt("colorLeft_", Color.rgb(50, 50, 50)),
                            preferences.getInt("color_", Color.rgb(50, 50, 50)),
                            preferences.getInt("colorRight_", Color.rgb(50, 50, 50)))
                    preferences.edit().putBoolean("changed", true).apply()
                    preferences.edit().putBoolean("changedWallpaper", true).apply()
                }

                val satGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                        0.0f, intArrayOf(Color.GRAY, getHSLColor(matiz, 1f, 0.5f)),
                        null, TileMode.CLAMP)
                val shapeSat = ShapeDrawable(RectShape())
                shapeSat.paint.shader = satGradient
                cpSaturacion.progressDrawable = shapeSat

                val lumGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                        0.0f, intArrayOf(Color.BLACK, getHSLColor(matiz, saturacion, 0.5f), Color.WHITE),
                        null, TileMode.CLAMP)
                val shapeLum = ShapeDrawable(RectShape())
                shapeLum.paint.shader = lumGradient
                cpLuminosidad.progressDrawable = shapeLum

                view?.let {
                    it.background.setColorFilter(getHSLColor(matiz, saturacion, luminosidad), PorterDuff.Mode.SRC_IN)
                    it.setColorFilter(if (isBrightColor(getHSLColor(matiz, saturacion, luminosidad))) Color.BLACK else Color.WHITE)
                }
            }
        })


        openColorPicker()

        okColorPicker.setOnClickListener {

            if (lastColors.size>=3){
                lastColors.pollFirst()
            }
            lastColors.add(getHSLColor(matiz, saturacion, luminosidad))

            if (settingsVisible){
                closeColorPicker{openSettings()}
            }
            else if (widgetsVisible){
                closeColorPicker{openWidgets()}
            }
            else{
                closeColorPicker{}
            }
        }

    }

    fun setLastColors(){
        if (lastColors.size==0){
            lastColorsLL.visibility = GONE
            return
        }
        lastColorsLL.visibility = VISIBLE
        if (lastColors.size==1){
            lastColor1.visibility = VISIBLE
            lastColor2.visibility = GONE
            lastColor3.visibility = GONE
            lastColor1.setColorFilter(lastColors.get(0), PorterDuff.Mode.MULTIPLY)
        }
        else if (lastColors.size==2){
            lastColor1.visibility = VISIBLE
            lastColor2.visibility = VISIBLE
            lastColor3.visibility = GONE
            lastColor1.setColorFilter(lastColors.get(0), PorterDuff.Mode.MULTIPLY)
            lastColor2.setColorFilter(lastColors.get(1), PorterDuff.Mode.MULTIPLY)
        } else{
            lastColor1.visibility = VISIBLE
            lastColor2.visibility = VISIBLE
            lastColor3.visibility = VISIBLE
            lastColor1.setColorFilter(lastColors.get(0), PorterDuff.Mode.MULTIPLY)
            lastColor2.setColorFilter(lastColors.get(1), PorterDuff.Mode.MULTIPLY)
            lastColor3.setColorFilter(lastColors.get(2), PorterDuff.Mode.MULTIPLY)
        }


        lastColor1.setOnClickListener {
            var colorComponents = FloatArray(3)
            ColorUtils.colorToHSL(lastColors.get(0), colorComponents)
            cpColor.progress = colorComponents.get(0).toInt()
            cpSaturacion.progress = Math.round(colorComponents.get(1) * 100).toInt()
            cpLuminosidad.progress = Math.round(colorComponents.get(2) * 100).toInt()
        }
        lastColor2.setOnClickListener {
            var colorComponents = FloatArray(3)
            ColorUtils.colorToHSL(lastColors.get(1), colorComponents)
            cpColor.progress = colorComponents.get(0).toInt()
            cpSaturacion.progress = Math.round(colorComponents.get(1) * 100).toInt()
            cpLuminosidad.progress = Math.round(colorComponents.get(2) * 100).toInt()
        }
        lastColor3.setOnClickListener {
            var colorComponents = FloatArray(3)
            ColorUtils.colorToHSL(lastColors.get(2), colorComponents)
            cpColor.progress = colorComponents.get(0).toInt()
            cpSaturacion.progress = Math.round(colorComponents.get(1) * 100).toInt()
            cpLuminosidad.progress = Math.round(colorComponents.get(2) * 100).toInt()
        }
    }

    fun vibra(tiempo: Long){
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
        }else{
            v.vibrate(VibrationEffect.createOneShot(tiempo, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    fun vibraFuerte(){
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        }else{
            v.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    fun getHSLColor(h: Float, s: Float, l: Float):Int{
        var colorComponents_ = FloatArray(3)
        colorComponents_.set(0, h)
        colorComponents_.set(1, s)
        colorComponents_.set(2, l)
        var color = ColorUtils.HSLToColor(colorComponents_)
        return color
    }

}


