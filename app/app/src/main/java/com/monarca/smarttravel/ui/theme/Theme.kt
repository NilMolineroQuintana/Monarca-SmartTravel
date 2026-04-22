package com.monarca.smarttravel.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    // Primarios
    primary = MagentaRose,          // Color principal mode clar
    onPrimary = PureWhite,
    primaryContainer = PeachyPink,
    onPrimaryContainer = RichBlack,

    // Secundarios
    secondary = MauveAccent,
    onSecondary = PureWhite,
    secondaryContainer = MauveAccent,
    onSecondaryContainer = RichBlack,

    // Terciarios
    tertiary = RoyalPurple,
    onTertiary = PureWhite,
    tertiaryContainer = SoftTaupe,
    onTertiaryContainer = RichBlack,

    // Fondos
    background = CreamWhite,
    onBackground = RichBlack,

    // Superficies
    surface = PureWhite,
    onSurface = RichBlack,
    surfaceVariant = LightBeige,
    onSurfaceVariant = DarkCharcoal,

    // Error
    error = CrimsonRed,
    onError = PureWhite,

    // Otros
    outline = MediumGray,
    outlineVariant = LightBeige
)

private val DarkColorScheme = darkColorScheme(
    // Primarios
    primary = PeachyPink,           // Color principal mode oscur
    onPrimary = RichBlack,
    primaryContainer = MagentaRose,
    onPrimaryContainer = CreamWhite,

    // Secundarios
    secondary = MauveAccent,
    onSecondary = CreamWhite,
    secondaryContainer = RoyalPurple,
    onSecondaryContainer = CreamWhite,

    // Terciarios
    tertiary = SoftTaupe,
    onTertiary = RichBlack,
    tertiaryContainer = MauveAccent,
    onTertiaryContainer = CreamWhite,

    // Fondos
    background = Color(0xFF1A1716),
    onBackground = CreamWhite,

    // Superficies
    surface = Color(0xFF2A2422),
    onSurface = CreamWhite,
    surfaceVariant = Color(0xFF3E3835),
    onSurfaceVariant = LightBeige,

    // Error
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun MonarcaSmartTravelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}