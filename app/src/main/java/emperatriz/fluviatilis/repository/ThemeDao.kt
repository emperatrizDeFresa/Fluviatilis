package emperatriz.fluviatilis.repository

import androidx.room.*

@Dao
interface ThemeDao {

    @Query("SELECT * FROM theme")
    suspend fun getAll(): List<Theme>

    @Insert
    suspend fun insert(theme: Theme)

    @Update
    suspend fun update(theme: Theme)

    @Delete
    suspend fun delete(theme: Theme)

}