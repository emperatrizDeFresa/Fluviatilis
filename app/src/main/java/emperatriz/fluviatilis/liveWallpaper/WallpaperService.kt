package emperatriz.fluviatilis.liveWallpaper

class WallpaperService : BaseWallpaperService() {
    override fun createWallpaperRenderer(): WallpaperRenderer {
        return WallpaperDrawer(applicationContext, true) //TODO return your WallpaperRenderer implementation here
    }
}