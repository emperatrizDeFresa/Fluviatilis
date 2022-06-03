package emperatriz.fluviatilis.repository

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun getAll(): List<Theme> = appDatabase.themeDao().getAll()

    override suspend fun insert(theme: Theme) = appDatabase.themeDao().insert(theme)

    override suspend fun update(theme: Theme) = appDatabase.themeDao().update(theme)

    override suspend fun delete(theme: Theme) = appDatabase.themeDao().delete(theme)
}