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
    primary = FilyzonCyan,
    onPrimary = Color(0xFF003549),
    primaryContainer = FilyzonBluePrimary,
    onPrimaryContainer = Color.White,
    secondary = GoldVip,
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = Color(0xFF5B4300),
    onSecondaryContainer = GoldVipLight,
    tertiary = EasyPaisaGreen,
    background = FilyzonBackgroundDark,
    onBackground = TextPrimaryDark,
    surface = FilyzonSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = FilyzonCardDark,
    onSurfaceVariant = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = FilyzonBluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = GoldVip,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFDEA3),
    onSecondaryContainer = Color(0xFF261900),
    tertiary = EasyPaisaGreen,
    background = FilyzonBackgroundLight,
    onBackground = TextPrimaryLight,
    surface = FilyzonSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFDEE3EA),
    onSurfaceVariant = TextSecondaryLight
)

@Composable
fun FilyzonTheme(
    darkTheme: Boolean = true, // Default to sleek dark mode matching the logo & entertainment/TikTok feel
    dynamicColor: Boolean = false,
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

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    FilyzonTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
