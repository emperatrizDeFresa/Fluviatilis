package emperatriz.fluviatilis.settings

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
import android.util.DisplayMetrics
import android.view.View
import android.view.View.*
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.graphics.ColorUtils
import emperatriz.fluviatilis.liveWallpaper.R
import emperatriz.fluviatilis.liveWallpaper.WallpaperDrawer
import emperatriz.fluviatilis.liveWallpaper.WallpaperService
import emperatriz.fluviatilis.widgets.pypots.SysPypots
import emperatriz.fluviatilis.widgets.spin.SpinDrawUtils
import kotlinx.android.synthetic.main.activity_settings.*
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver


class SettingsActivity : BaseSettingsActivity() {

    var settingsVisible=false;
    var widgetsVisible=false;
    var colorPickerVisible=false;

    lateinit var colorGradient: LinearGradient

    override val settingsActivityComponent by lazy { ComponentName(this, "$packageName.LauncherSettingsActivity") }
    override val wallpaperServiceComponent by lazy { ComponentName(this, WallpaperService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings_live_wallpaper.renderer = WallpaperDrawer(applicationContext, false)
        colorGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (widgetsPanel.paddingLeft + widgetsPanel.paddingRight + spinColor.paddingLeft.toFloat() + spinColor.paddingRight.toFloat()),
                0.0f, intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED),
                null, TileMode.CLAMP)

    }


    private fun closeWidgets(lambda: () -> Unit){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.VISIBLE
        colorPicker.visibility = GONE
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
    }

    private fun closeSettings(lambda: () -> Unit){
        settingsPanel.visibility = View.VISIBLE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = GONE
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
    }

    private fun closeColorPicker(lambda: () -> Unit){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = VISIBLE
        val anim = ObjectAnimator.ofFloat(settingsPanel, "alpha", 0f)
        anim.duration = 300
        anim.start()
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                colorPicker.setVisibility(View.INVISIBLE)
                lambda()
            }
        })
        colorPickerVisible=false
    }

    private fun openWidgets(){
        separatorWidth.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvWeight()
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.VISIBLE
        colorPicker.visibility = GONE
        val anim = ObjectAnimator.ofFloat(widgetsPanel, "alpha", 1f)
        anim.duration = 300
        anim.start()
        widgetsVisible=true

        val shape = ShapeDrawable(RectShape())
        shape.paint.shader = colorGradient
        spinColor.progressDrawable = shape
    }

    private fun openSettings(){
        separatorWidth.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvWeight()
        settingsPanel.visibility = View.VISIBLE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = GONE
        val anim = ObjectAnimator.ofFloat(settingsPanel, "alpha", 1f)
        anim.duration = 300
        anim.start()
        settingsVisible=true
    }

    private fun openColorPicker(){
        separatorWidth.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvWeight()
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = VISIBLE
        val anim = ObjectAnimator.ofFloat(colorPicker, "alpha", 1f)
        anim.duration = 300
        anim.start()
        colorPickerVisible=true
    }

    override fun onResume() {
        super.onResume()

        val preferences = getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)


        




        floatingActionButton.setOnClickListener {
            if (widgetsVisible){
                closeWidgets{openSettings()}
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
            else{
                if (widgetsVisible){
                    closeWidgets{}
                }
                else{
                    openWidgets()
                }
            }
        }

        separatorWidth.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvWeight()
        separatorWidth.progress = preferences.getInt("fluvWeight", 12)
        separatorWidth.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(settingsPanel, settingsPanel2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, settingsPanel, settingsPanel2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvWeight = Math.max(1, progress)
                preferences.edit().putInt("fluvWeight", Math.max(1, progress)).apply()
                preferences.edit().putBoolean("changed", true).apply()
            }
        })

        fluvNumber.min = 12
        fluvNumber.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvNumber()
        fluvNumber.progress = preferences.getInt("fluvNumber", 16)
        fluvNumber.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(settingsPanel, settingsPanel2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, settingsPanel, settingsPanel2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvNumber = Math.max(12, progress)
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight = Math.round(((settings_live_wallpaper.renderer as WallpaperDrawer).model.height * ((preferences.getInt("heightness", 15)).toFloat() / 100)) / Math.max(12, progress)).toInt()
                wideness.min = (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight * 2
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.initFluvs()
                preferences.edit().putInt("fluvNumber", Math.max(12, progress)).apply()
                preferences.edit().putBoolean("changed", true).apply()
                separatorWidth.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvWeight()
            }
        })

        speed.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxSpeed()
        speed.progress = preferences.getInt("speed", 2)
        speed.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(settingsPanel, settingsPanel2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, settingsPanel, settingsPanel2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.speed = progress
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.initFluvs()
                preferences.edit().putInt("speed", progress).apply()
                preferences.edit().putBoolean("changed", true).apply()
            }
        })

        wideness.min = (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight*4
        val dm = DisplayMetrics()
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
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.wideness = Math.max(1, progress)
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.initFluvs()
                preferences.edit().putInt("wideness", Math.max(1, progress)).apply()
                preferences.edit().putBoolean("changed", true).apply()
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
                preferences.edit().putInt("heightness", Math.max(1, progress)).apply()
//                fluvNumber.progress = Math.min(Math.max(12, Math.round((dm.heightPixels * ((progress).toFloat() / 100)) / (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight)),
//                        (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvNumber())
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight = Math.round(((settings_live_wallpaper.renderer as WallpaperDrawer).model.height * ((Math.max(1, progress)).toFloat() / 100)) / fluvNumber.progress).toInt()
                wideness.min = (settings_live_wallpaper.renderer as WallpaperDrawer).model.fluvHeight * 2
                (settings_live_wallpaper.renderer as WallpaperDrawer).model.initFluvs()

                separatorWidth.max = (settings_live_wallpaper.renderer as WallpaperDrawer).model.maxFluvWeight()
                preferences.edit().putBoolean("changed", true).apply()

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
                preferences.edit().putInt("rotation", progress).apply()
                preferences.edit().putBoolean("changed", true).apply()
            }
        })


        colorMiddle.background.setColorFilter(preferences.getInt("color", Color.BLACK), PorterDuff.Mode.SRC_IN)
        colorMiddle.setColorFilter(if (isBrightColor(preferences.getInt("color", Color.BLACK))) Color.BLACK else Color.WHITE)
        colorMiddle.setOnClickListener {

            setColorFor(preferences, "color", Color.valueOf(0xffff00ff.toInt()))

//            ColorPickerPopup.Builder(this)
//                    .initialColor(preferences.getInt("color", Color.BLACK)) // Set initial color
//                    .enableBrightness(true) // Enable brightness slider or not
//                    .enableAlpha(false) // Enable alpha slider or not
//                    .okTitle("Seleccionar")
//                    .cancelTitle("")
//                    .showIndicator(true)
//                    .showValue(false)
//                    .build()
//                    .show(colorMiddle, object : ColorPickerObserver() {
//                        override fun onColorPicked(color: Int) {
//                            colorMiddle.background.setColorFilter(color, PorterDuff.Mode.SRC_IN)
//                            colorMiddle.setColorFilter(if (isBrightColor(color)) Color.BLACK else Color.WHITE)
//                            preferences.edit().putInt("color", color).apply()
//                            preferences.edit().putBoolean("changed", true).apply()
//                        }
//
//                        fun onColor(color: Int, fromUser: Boolean) {}
//                    })
            }

        colorLeft.background.setColorFilter(preferences.getInt("colorLeft", Color.rgb(50, 50, 50)), PorterDuff.Mode.SRC_IN)
        colorLeft.setColorFilter(if (isBrightColor(preferences.getInt("colorLeft", Color.rgb(50, 50, 50)))) Color.BLACK else Color.WHITE)
        colorLeft.setOnClickListener {
            ColorPickerPopup.Builder(this)
                    .initialColor(preferences.getInt("colorLeft", Color.BLACK)) // Set initial color
                    .enableBrightness(true) // Enable brightness slider or not
                    .enableAlpha(false) // Enable alpha slider or not
                    .okTitle("Seleccionar")
                    .cancelTitle("")
                    .showIndicator(true)
                    .showValue(false)
                    .build()
                    .show(colorLeft, object : ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            colorLeft.background.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                            colorLeft.setColorFilter(if (isBrightColor(color)) Color.BLACK else Color.WHITE)
                            preferences.edit().putInt("colorLeft", color).apply()
                            preferences.edit().putBoolean("changed", true).apply()
                        }

                        fun onColor(color: Int, fromUser: Boolean) {}
                    })
        }

        colorRight.background.setColorFilter(preferences.getInt("colorRight", Color.rgb(100, 100, 100)), PorterDuff.Mode.SRC_IN)
        colorRight.setColorFilter(if (isBrightColor(preferences.getInt("colorRight", Color.rgb(100, 100, 100)))) Color.BLACK else Color.WHITE)
        colorRight.setOnClickListener {
            ColorPickerPopup.Builder(this)
                    .initialColor(preferences.getInt("colorRight", Color.BLACK)) // Set initial color
                    .enableBrightness(true) // Enable brightness slider or not
                    .enableAlpha(false) // Enable alpha slider or not
                    .okTitle("Seleccionar")
                    .cancelTitle("")
                    .showIndicator(true)
                    .showValue(false)
                    .build()
                    .show(colorRight, object : ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            colorRight.background.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                            colorRight.setColorFilter(if (isBrightColor(color)) Color.BLACK else Color.WHITE)
                            preferences.edit().putInt("colorRight", color).apply()
                            preferences.edit().putBoolean("changed", true).apply()
                        }

                        fun onColor(color: Int, fromUser: Boolean) {}
                    })
        }


        if (preferences.getBoolean("showWidget", false)){
            widgetClock.visibility = VISIBLE
        }
        else{
            widgetClock.visibility = INVISIBLE
        }

        showWidgetLL.setOnClickListener{
            showWidget.performClick()
        }

        (showWidget as CheckBox).isChecked = preferences.getBoolean("showWidget", false)
        showWidget.setOnClickListener{
            preferences.edit().putBoolean("showWidget", (it as CheckBox).isChecked).apply()
            if ((it as CheckBox).isChecked){
                widgetClock.visibility = VISIBLE
            }
            else{
                widgetClock.visibility = INVISIBLE
            }
        }


        widgetX.max = dm.widthPixels
        widgetX.progress = preferences.getInt("widgetX", 0)+preferences.getInt("widgetXsize", 454)/2
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
                preferences.edit().putInt("widgetX", progress - widgetXsize.progress / 2).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetY.max = dm.heightPixels
        widgetY.progress = preferences.getInt("widgetY", 0)+preferences.getInt("widgetXsize", 454)/2
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
                preferences.edit().putInt("widgetY", progress - widgetXsize.progress / 2).apply()
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
                preferences.edit().putInt("widgetXsize", progress).apply()
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

        haloLL.setOnLongClickListener{
            val popupMenu = PopupMenu(this@SettingsActivity, it)
            var i = 0
            for (item in resources.getStringArray(R.array.ceroseis)) {
                popupMenu.menu.add(i, i, i, item)
                i++
            }
            popupMenu.setOnMenuItemClickListener { item ->
                SysPypots.save(SysPypots.SETTINGS_DIVISIONES, item.groupId, this@SettingsActivity)
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
                false
            }
            popupMenu.show()
            false
        }




        widgetX2.max = dm.widthPixels
        widgetX2.progress = preferences.getInt("widgetX", 0)+preferences.getInt("widgetXsize", 454)/2
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
                preferences.edit().putInt("widgetX", progress - widgetXsize2.progress / 2).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetY2.max = dm.heightPixels
        widgetY2.progress = preferences.getInt("widgetY", 0)+preferences.getInt("widgetXsize", 454)/2
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
                preferences.edit().putInt("widgetY", progress - widgetXsize2.progress / 2).apply()
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
                preferences.edit().putInt("outmode", progress).apply()

            }
        })

        val shape = ShapeDrawable(RectShape())
        shape.paint.shader = colorGradient
        spinColor.progressDrawable = shape
        spinColor.progress = preferences.getInt(SpinDrawUtils.ACCENT_COLOR, 0xffff00ff.toInt())
        spinColor.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.VISIBLE
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, widgetsPanel, spinSettings_)
                widgetsPanel2.visibility = View.INVISIBLE
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val color = getColorFromSpinner(progress)
                preferences.edit().putInt(SpinDrawUtils.ACCENT_COLOR, color).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
            }
        })

        initWidgetSelector()
    }




    fun getColorFromSpinner(v: Int):Int{

//Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED

        var r:Int = 0
        var g:Int = 0
        var b:Int = 0

        val seccion = (v/256)+1
        val value = v % 256

        when(seccion){
            1->{
                r = 255
                g = value
            }
            2->{
                r = 256 - value
                g = 255
            }
            3->{
                g = 255
                b = value
            }
            4->{
                g = 256 - value
                b = 255
            }
            5->{
                r = value
                b = 255
            }
            6->{
                r = 255
                b = 256 - value
            }

        }

        r = r shl 16 and 0x00FF0000
        g = g shl 8 and 0x0000FF00
        b = b and 0x000000FF

        return 0xFF000000.toInt() or r or g or b
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

    private fun initWidgetSelector() {

        val preferences = getSharedPreferences("fluviatilis", Context.MODE_PRIVATE)

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




        pypots.setOnClickListener {
            pypots.toColor()
            spin.toGrayscale()
            custom.toGrayscale()
            widgetList.smoothScrollTo(0, 0)
            pypotsSettings.visibility= VISIBLE
            spinSettings.visibility = GONE
            customSettings.visibility = GONE
            preferences.edit().putString("selectedWidget", "pypots").apply()
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
        for (i in 0 until panel2.childCount){
            panel2.getChildAt(i)?.visibility=View.VISIBLE
        }
    }



    fun setColorFor(preferences: SharedPreferences, preferencesKey:String, currentColor:Color){

        val cpGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                0.0f, intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED),
                null, TileMode.CLAMP)
        val shape = ShapeDrawable(RectShape())
        shape.paint.shader = cpGradient
        cpColor.progressDrawable = shape


        openColorPicker()

        var colorComponents = FloatArray(3)
        ColorUtils.colorToHSL(currentColor.toArgb(), colorComponents)


        cpColor.progress = Math.round(colorComponents.get(0))
        cpColor.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(colorPicker, colorPicker)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, colorPicker, colorPicker)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                colorComponents.set(0,progress.toFloat())
                val color = ColorUtils.HSLToColor(colorComponents)
                preferences.edit().putInt(preferencesKey, color).apply()
                //preferences.edit().putBoolean("changed", true).apply()
            }
        })

    }


}


