package emperatriz.fluviatilis.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import emperatriz.fluviatilis.repository.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SettingsViewModel : ViewModel() {

    fun populateThemes() = viewModelScope.launch {
        populateThemesPre()
        val result = populateThemesWork()
        populateThemesPost(result)
    }

    private suspend fun populateThemesWork(): List<Theme> = withContext(Dispatchers.IO) {

        return@withContext ArrayList()
    }

    private fun populateThemesPre() {

    }

    private fun populateThemesPost(result: List<Theme>) {

    }
}