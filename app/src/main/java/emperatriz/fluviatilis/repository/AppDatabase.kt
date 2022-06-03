package emperatriz.fluviatilis.repository

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Theme::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun themeDao(): ThemeDao

}