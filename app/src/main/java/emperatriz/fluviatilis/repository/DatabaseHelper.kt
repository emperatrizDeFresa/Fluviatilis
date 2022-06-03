package emperatriz.fluviatilis.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface DatabaseHelper {

    suspend fun getAll(): List<Theme>

    suspend fun insert(theme: Theme)

    suspend fun update(theme: Theme)

    suspend fun delete(theme: Theme)

}