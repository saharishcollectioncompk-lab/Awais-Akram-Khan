package com.example.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "portfolio_settings")

class ThemePreferenceRepository(private val context: Context) {
    private val darkThemeKey = booleanPreferencesKey("dark_theme_override")

    val darkThemeOverride: Flow<Boolean?> = context.themeDataStore.data.map { prefs ->
        if (prefs.contains(darkThemeKey)) prefs[darkThemeKey] else null
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.themeDataStore.edit { prefs ->
            prefs[darkThemeKey] = isDark
        }
    }
}
