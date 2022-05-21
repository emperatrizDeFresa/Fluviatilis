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



class SettingsActivity : BaseSettingsActivity() {

    var settingsVisible=false;
    var widgetsVisible=false;
    var colorPickerVisible=false;


    override val settingsActivityComponent by lazy { ComponentName(this, "$packageName.LauncherSettingsActivity") }
    override val wallpaperServiceComponent by lazy { ComponentName(this, WallpaperService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings_live_wallpaper.renderer = WallpaperDrawer(applicationContext, false)
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
        colorPicker.visibility = INVISIBLE
        colorPickerVisible=false
        lambda()
    }

    private fun openWidgets(){
        settingsPanel.visibility = View.GONE
        widgetsPanel.visibility = View.VISIBLE
        colorPicker.visibility = GONE
        val anim = ObjectAnimator.ofFloat(widgetsPanel, "alpha", 1f)
        anim.duration = 300
        anim.start()
        widgetsVisible=true
        val shape = ShapeDrawable(RectShape())
        initWidgetSelector()
    }

    private fun openSettings(){
        settingsPanel.visibility = View.VISIBLE
        widgetsPanel.visibility = View.GONE
        colorPicker.visibility = GONE
        val anim = ObjectAnimator.ofFloat(settingsPanel, "alpha", 1f)
        anim.duration = 300
        anim.start()
        settingsVisible=true
    }

    private fun openColorPicker(){
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
            else if (colorPickerVisible){
                closeColorPicker{openSettings()}
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

                val diff = Math.round((preferences.getInt("widgetXsize", 454)-progress)/2f)


                preferences.edit().putInt("widgetX", widgetX.progress+diff).apply()
                preferences.edit().putInt("widgetY",  widgetY.progress+diff).apply()

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

                val diff = Math.round((preferences.getInt("widgetXsize", 454)-progress)/2f)


                preferences.edit().putInt("widgetX", widgetX2.progress+diff).apply()
                preferences.edit().putInt("widgetY",  widgetY2.progress+diff).apply()

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

        colorSpin.background.setColorFilter(preferences.getInt(SpinDrawUtils.ACCENT_COLOR, Color.BLACK), PorterDuff.Mode.SRC_IN)
        colorSpin.setColorFilter(if (isBrightColor(preferences.getInt(SpinDrawUtils.ACCENT_COLOR, Color.BLACK))) Color.BLACK else Color.WHITE)
        colorSpin.setOnClickListener { setColorFor(preferences,SpinDrawUtils.ACCENT_COLOR, it!! as ImageButton ) }



        widgetX3.max = dm.widthPixels
        widgetX3.progress = preferences.getInt("widgetX", 0)-preferences.getInt("widgetXsize", 454)/2
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
                preferences.edit().putInt("widgetX", progress ).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        widgetY3.max = dm.heightPixels
        widgetY3.progress = preferences.getInt("widgetY", 0)-preferences.getInt("widgetXsize", 454)/2
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

                val diff = Math.round((preferences.getInt("widgetXsize", 454)-progress)/2f)


                preferences.edit().putInt("widgetX", widgetX3.progress+diff).apply()
                preferences.edit().putInt("widgetY",  widgetY3.progress+diff).apply()

                preferences.edit().putInt("widgetXsize", progress).apply()
                preferences.edit().putBoolean("changedWidget", true).apply()
                preferences.edit().putBoolean("changedWidgetWallpaper", true).apply()
            }
        })

        customAgujas.background.setColorFilter(preferences.getInt("colorAgujas", Color.BLACK), PorterDuff.Mode.SRC_IN)
        customAgujas.setColorFilter(if (isBrightColor(preferences.getInt("colorAgujas", Color.BLACK))) Color.BLACK else Color.WHITE)
        customAgujas.setOnClickListener { setColorFor(preferences,"colorAgujas", it!! as ImageButton ) }

        customDial.background.setColorFilter(preferences.getInt("colorEsfera", Color.WHITE), PorterDuff.Mode.SRC_IN)
        customDial.setColorFilter(if (isBrightColor(preferences.getInt("colorEsfera", Color.WHITE))) Color.BLACK else Color.WHITE)
        customDial.setOnClickListener { setColorFor(preferences,"colorEsfera", it!! as ImageButton ) }

        customMarcadores.background.setColorFilter(preferences.getInt("colorBorde", Color.DKGRAY), PorterDuff.Mode.SRC_IN)
        customMarcadores.setColorFilter(if (isBrightColor(preferences.getInt("colorBorde", Color.DKGRAY))) Color.BLACK else Color.WHITE)
        customMarcadores.setOnClickListener { setColorFor(preferences,"colorBorde", it!! as ImageButton ) }

        customSegundero.background.setColorFilter(preferences.getInt("colorSegundo", Color.RED), PorterDuff.Mode.SRC_IN)
        customSegundero.setColorFilter(if (isBrightColor(preferences.getInt("colorSegundo", Color.RED))) Color.BLACK else Color.WHITE)
        customSegundero.setOnClickListener { setColorFor(preferences,"colorSegundo", it!! as ImageButton ) }

        initWidgetSelector()
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



    fun setColorFor(preferences: SharedPreferences, preferencesKey:String, view:ImageButton){


        cpColor.setOnSeekBarChangeListener(null)
        cpSaturacion.setOnSeekBarChangeListener(null)
        cpLuminosidad.setOnSeekBarChangeListener(null)

        var currentColor = Color.valueOf(preferences.getInt(preferencesKey,Color.WHITE))

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








        cpSaturacion.progress = Math.round(saturacion*100)
        cpSaturacion.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(colorPicker2, colorPicker2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, colorPicker2, colorPicker2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                saturacion = progress/100f
                preferences.edit().putInt(preferencesKey, getHSLColor(matiz, saturacion, luminosidad)).apply()

                val lumGradient = LinearGradient(0f, 0f, window.windowManager.defaultDisplay.width - (colorPicker.paddingLeft + colorPicker.paddingRight + cpColor.paddingLeft.toFloat() + cpColor.paddingRight.toFloat()),
                        0.0f, intArrayOf(Color.BLACK, getHSLColor(matiz, saturacion, 0.5f), Color.WHITE),
                        null, TileMode.CLAMP)
                val shapeLum = ShapeDrawable(RectShape())
                shapeLum.paint.shader = lumGradient
                cpLuminosidad.progressDrawable = shapeLum

                view?.let{
                    it.background.setColorFilter(getHSLColor(matiz, saturacion, luminosidad), PorterDuff.Mode.SRC_IN)
                    it.setColorFilter(if (isBrightColor(getHSLColor(matiz, saturacion, luminosidad))) Color.BLACK else Color.WHITE)
                }
            }
        })

        cpLuminosidad.progress = Math.round(luminosidad*100)
        cpLuminosidad.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(colorPicker2, colorPicker2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, colorPicker2, colorPicker2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                luminosidad = progress/100f
                preferences.edit().putInt(preferencesKey, getHSLColor(matiz, saturacion, luminosidad)).apply()

                view?.let{
                    it.background.setColorFilter(getHSLColor(matiz, saturacion, luminosidad), PorterDuff.Mode.SRC_IN)
                    it.setColorFilter(if (isBrightColor(getHSLColor(matiz, saturacion, luminosidad))) Color.BLACK else Color.WHITE)
                }
            }
        })

        cpColor.progress = Math.round(matiz)
        cpColor.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                showAll(colorPicker2, colorPicker2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                showOnlyMe(seekBar, colorPicker2, colorPicker2)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                matiz = progress.toFloat()
                preferences.edit().putInt(preferencesKey, getHSLColor(matiz, saturacion, luminosidad)).apply()

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

                view?.let{
                    it.background.setColorFilter(getHSLColor(matiz, saturacion, luminosidad), PorterDuff.Mode.SRC_IN)
                    it.setColorFilter(if (isBrightColor(getHSLColor(matiz, saturacion, luminosidad))) Color.BLACK else Color.WHITE)
                }
            }
        })


        openColorPicker()

        okColorPicker.setOnClickListener {




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

    fun getHSLColor(h:Float, s:Float, l:Float):Int{
        var colorComponents_ = FloatArray(3)
        colorComponents_.set(0,h)
        colorComponents_.set(1,s)
        colorComponents_.set(2,l)
        var color = ColorUtils.HSLToColor(colorComponents_)
        return color
    }

}


