package emperatriz.fluviatilis.liveWallpaper

import android.content.Context

class WallpaperService : BaseWallpaperService() {
    override fun createWallpaperRenderer(): WallpaperRenderer {
        return WallpaperDrawer(applicationContext, true)
    }
}