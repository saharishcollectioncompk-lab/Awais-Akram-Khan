package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PortfolioExtendedColors(
    val sectionBackground: Color,
    val cardBackground: Color,
    val elevatedSurface: Color,
    val secondaryText: Color,
    val borderColor: Color,
    val secondaryAccent: Color,
    val isDark: Boolean
)

val LocalPortfolioColors = staticCompositionLocalOf {
    PortfolioExtendedColors(
        sectionBackground = LightSectionBg,
        cardBackground = LightCardBg,
        elevatedSurface = LightElevatedBg,
        secondaryText = LightTextSecondary,
        borderColor = LightBorder,
        secondaryAccent = LightSecondaryBlue,
        isDark = false
    )
}

private val LightColorScheme = lightColorScheme(
    primary = LightAccentBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = LightTextPrimary,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightCardBg,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSectionBg,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    error = ErrorCrimson,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkAccentBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = DarkTextMain,
    secondary = DarkSecondaryAccent,
    onSecondary = DarkMainBackground,
    background = DarkMainBackground,
    onBackground = DarkTextMain,
    surface = DarkCardBackground,
    onSurface = DarkTextMain,
    surfaceVariant = DarkSectionBackground,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    error = ErrorCrimson,
    onError = Color.White
)

@Composable
fun AwaisPortfolioTheme(
    darkThemeOverride: Boolean? = null,
    content: @Composable () -> Unit
) {
    val useDarkTheme = darkThemeOverride ?: isSystemInDarkTheme()
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (useDarkTheme) {
        PortfolioExtendedColors(
            sectionBackground = DarkSectionBackground,
            cardBackground = DarkCardBackground,
            elevatedSurface = Color(0xFF223759),
            secondaryText = DarkTextSecondary,
            borderColor = DarkBorder,
            secondaryAccent = DarkSecondaryAccent,
            isDark = true
        )
    } else {
        PortfolioExtendedColors(
            sectionBackground = LightSectionBg,
            cardBackground = LightCardBg,
            elevatedSurface = LightElevatedBg,
            secondaryText = LightTextSecondary,
            borderColor = LightBorder,
            secondaryAccent = LightSecondaryBlue,
            isDark = false
        )
    }

    CompositionLocalProvider(LocalPortfolioColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PortfolioTypography,
            content = content
        )
    }
}
