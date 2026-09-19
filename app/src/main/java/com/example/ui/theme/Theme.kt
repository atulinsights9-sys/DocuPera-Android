package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RoyalBlue400,
    onPrimary = Navy900,
    primaryContainer = Navy700,
    onPrimaryContainer = RoyalBlue100,
    secondary = Slate400,
    onSecondary = Color.White,
    secondaryContainer = Navy600,
    onSecondaryContainer = Slate100,
    tertiary = Amber500,
    onTertiary = Navy900,
    background = Navy900,
    onBackground = Slate50,
    surface = Navy800,
    onSurface = Slate50,
    surfaceVariant = Navy700,
    onSurfaceVariant = Slate200,
    outline = Navy600,
    outlineVariant = Navy700
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue600,
    onPrimary = Color.White,
    primaryContainer = RoyalBlue100,
    onPrimaryContainer = RoyalBlue600,
    secondary = Slate500,
    onSecondary = Color.White,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate900,
    tertiary = Amber500,
    onTertiary = Color.White,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate500,
    outline = Slate200,
    outlineVariant = Slate100
)

@Composable
fun DocuPeraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve brand palette consistency
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatible alias for existing references
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) = DocuPeraTheme(darkTheme, dynamicColor, content)
